package com.mefy.platemate.presentation.features.main.messages.chatdetail

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.domain.usecase.block.BlockUserUseCase
import com.mefy.platemate.domain.usecase.block.UnblockUserUseCase
import com.mefy.platemate.domain.usecase.chat.GetChatRoomUseCase
import com.mefy.platemate.domain.usecase.chat.LeaveChatUseCase
import com.mefy.platemate.domain.usecase.report.ReportUserUseCase
import com.mefy.platemate.presentation.common.dialog.DialogFactory
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.ChatDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getChatRoomUseCase: GetChatRoomUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val leaveChatUseCase: LeaveChatUseCase,
    private val reportUserUseCase: ReportUserUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: ChatDetailDestination = savedStateHandle.toRoute()
    private val roomId: Long = route.conversationId.toLongOrNull() ?: 0L

    private val _uiState = MutableStateFlow(
        ChatDetailUiState(
            conversationId = route.conversationId,
            participantName = route.participantName,
        )
    )
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ChatDetailUiEffect>()
    val uiEffect: SharedFlow<ChatDetailUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadRoom()
    }

    fun onAction(action: ChatDetailUiAction) {
        when (action) {
            ChatDetailUiAction.BackClicked ->
                _uiEffect.emitUiEffect(ChatDetailUiEffect.NavigateBack)

            ChatDetailUiAction.MessageClicked ->
                _uiEffect.emitUiEffect(ChatDetailUiEffect.NavigateBack)

            ChatDetailUiAction.ProfileClicked ->
                _uiState.value.otherUserId?.let { userId ->
                    _uiEffect.emitUiEffect(ChatDetailUiEffect.NavigateToUserProfile(userId))
                }

            ChatDetailUiAction.NotificationsToggled ->
                _uiState.update { it.copy(notificationsEnabled = !it.notificationsEnabled) }

            ChatDetailUiAction.DeleteChatClicked -> showDialog(
                DialogFactory.deleteChatConfirmDialog(
                    onConfirm = { onDeleteChatClicked() }
                )
            )
            ChatDetailUiAction.ReportClicked -> onReportClicked()
            ChatDetailUiAction.BlockClicked -> onBlockClicked()
        }
    }

    private fun loadRoom() {
        launch {
            when (val result = getChatRoomUseCase(roomId)) {
                is AppResult.Success -> _uiState.update { it.copy(otherUserId = result.data.otherParticipantId) }
                is AppResult.Error -> Unit
            }
        }
    }

    private fun onDeleteChatClicked() {
        _uiState.update { it.copy(isDeleting = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isDeleting = false) }
            handleError(error)
        }) {
            when (val result = leaveChatUseCase(roomId)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isDeleting = false) }
                    _uiEffect.emitUiEffect(ChatDetailUiEffect.NavigateToMessagesList)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isDeleting = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun onReportClicked() {
        val targetId = _uiState.value.otherUserId ?: return
        launch(onError = ::handleError) {
            when (val result = reportUserUseCase(targetId, REPORT_REASON_OTHER)) {
                is AppResult.Success -> showSuccess(UiText.Resource(R.string.user_profile_report_submitted))
                is AppResult.Error -> showError(result.error.toUiText())
            }
        }
    }

    private fun onBlockClicked() {
        val state = _uiState.value
        val targetId = state.otherUserId ?: return
        if (state.isBlockInProgress) return

        val wasBlocked = state.isBlocked
        _uiState.update { it.copy(isBlockInProgress = true) }
        launch {
            val result = if (wasBlocked) unblockUserUseCase(targetId) else blockUserUseCase(targetId)
            when (result) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isBlocked = !wasBlocked, isBlockInProgress = false)
                }
                is AppResult.Error -> _uiState.update { it.copy(isBlockInProgress = false) }
            }
        }
    }

    private companion object {
        const val REPORT_REASON_OTHER = "OTHER"
    }
}
