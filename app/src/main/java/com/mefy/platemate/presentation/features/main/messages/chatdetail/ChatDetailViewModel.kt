package com.mefy.platemate.presentation.features.main.messages.chatdetail

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.presentation.common.error.UiErrorResolver
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
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val route: ChatDetailDestination = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(
        ChatDetailUiState(
            conversationId = route.conversationId,
            participantName = route.participantName,
            initials = route.initials,
            avatarBg = Color(route.avatarBgArgb.toInt()),
            avatarFg = Color(route.avatarFgArgb.toInt())
        )
    )
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ChatDetailUiEffect>()
    val uiEffect: SharedFlow<ChatDetailUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: ChatDetailUiAction) {
        when (action) {
            ChatDetailUiAction.BackClicked ->
                _uiEffect.emitUiEffect(ChatDetailUiEffect.NavigateBack)

            ChatDetailUiAction.MessageClicked ->
                _uiEffect.emitUiEffect(ChatDetailUiEffect.NavigateBack)

            ChatDetailUiAction.NotificationsToggled ->
                _uiState.update { it.copy(notificationsEnabled = !it.notificationsEnabled) }

            ChatDetailUiAction.DeleteChatClicked -> Unit
            ChatDetailUiAction.ReportClicked -> Unit
        }
    }
}
