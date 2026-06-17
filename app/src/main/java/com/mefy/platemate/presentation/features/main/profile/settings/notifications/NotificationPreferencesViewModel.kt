package com.mefy.platemate.presentation.features.main.profile.settings.notifications

import com.mefy.platemate.R
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.settings.UserSettings
import com.mefy.platemate.domain.usecase.settings.GetSettingsUseCase
import com.mefy.platemate.domain.usecase.settings.UpdateSettingsUseCase
import com.mefy.platemate.presentation.common.error.ErrorContext
import com.mefy.platemate.presentation.common.error.UiErrorResolver
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
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
class NotificationPreferencesViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val _uiState = MutableStateFlow(NotificationPreferencesUiState())
    val uiState: StateFlow<NotificationPreferencesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<NotificationPreferencesUiEffect>()
    val uiEffect: SharedFlow<NotificationPreferencesUiEffect> = _uiEffect.asSharedFlow()

    init {
        load()
    }

    fun onAction(action: NotificationPreferencesUiAction) {
        when (action) {
            is NotificationPreferencesUiAction.MessagingChanged ->
                _uiState.updateWith(messagingEnabled = action.enabled)

            is NotificationPreferencesUiAction.MessageNotificationsChanged ->
                _uiState.updateWith(messageNotificationsEnabled = action.enabled)

            is NotificationPreferencesUiAction.FriendNotificationsChanged ->
                _uiState.updateWith(friendNotificationsEnabled = action.enabled)

            is NotificationPreferencesUiAction.NewFollowerChanged ->
                _uiState.updateWith(newFollowerEnabled = action.enabled)

            is NotificationPreferencesUiAction.PlateReviewChanged ->
                _uiState.updateWith(plateReviewEnabled = action.enabled)

            is NotificationPreferencesUiAction.ReviewReplyChanged ->
                _uiState.updateWith(reviewReplyEnabled = action.enabled)

            NotificationPreferencesUiAction.SaveClicked -> save()
        }
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true) }
        launch(onError = ::handleError) {
            when (val result = getSettingsUseCase()) {
                is AppResult.Success -> {
                    val settings = result.data.userSettings
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            messagingEnabled = settings.messagingEnabled,
                            messageNotificationsEnabled = settings.messageNotificationsEnabled,
                            friendNotificationsEnabled = settings.friendNotificationsEnabled,
                            initialMessagingEnabled = settings.messagingEnabled,
                            initialMessageNotificationsEnabled = settings.messageNotificationsEnabled,
                            initialFriendNotificationsEnabled = settings.friendNotificationsEnabled,
                            // backend desteklemediği alanlar için initial değer false kalır
                            initialNewFollowerEnabled = false,
                            initialPlateReviewEnabled = false,
                            initialReviewReplyEnabled = false,
                            hasChanges = false
                        )
                    }
                }

                is AppResult.Error -> {
                    handleError(result.error, context = ErrorContext.Profile)
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun save() {
        val state = _uiState.value
        if (!state.hasChanges || state.isSaving) return

        _uiState.update { it.copy(isSaving = true) }
        launch(onError = ::handleError) {
            when (
                val result = updateSettingsUseCase(
                    UserSettings(
                        messagingEnabled = state.messagingEnabled,
                        messageNotificationsEnabled = state.messageNotificationsEnabled,
                        friendNotificationsEnabled = state.friendNotificationsEnabled
                    )
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            initialMessagingEnabled = it.messagingEnabled,
                            initialMessageNotificationsEnabled = it.messageNotificationsEnabled,
                            initialFriendNotificationsEnabled = it.friendNotificationsEnabled,
                            initialNewFollowerEnabled = it.newFollowerEnabled,
                            initialPlateReviewEnabled = it.plateReviewEnabled,
                            initialReviewReplyEnabled = it.reviewReplyEnabled,
                            hasChanges = false
                        )
                    }
                    _uiEffect.emitUiEffect(
                        NotificationPreferencesUiEffect.ShowSnackbar(
                            UiText.Resource(R.string.profile_settings_saved)
                        )
                    )
                }

                is AppResult.Error -> {
                    handleError(result.error, context = ErrorContext.Profile)
                    _uiState.update { it.copy(isSaving = false) }
                }
            }
        }
    }

    private fun MutableStateFlow<NotificationPreferencesUiState>.updateWith(
        messagingEnabled: Boolean? = null,
        messageNotificationsEnabled: Boolean? = null,
        friendNotificationsEnabled: Boolean? = null,
        newFollowerEnabled: Boolean? = null,
        plateReviewEnabled: Boolean? = null,
        reviewReplyEnabled: Boolean? = null
    ) {
        update { current ->
            val next = current.copy(
                messagingEnabled = messagingEnabled ?: current.messagingEnabled,
                messageNotificationsEnabled = messageNotificationsEnabled ?: current.messageNotificationsEnabled,
                friendNotificationsEnabled = friendNotificationsEnabled ?: current.friendNotificationsEnabled,
                newFollowerEnabled = newFollowerEnabled ?: current.newFollowerEnabled,
                plateReviewEnabled = plateReviewEnabled ?: current.plateReviewEnabled,
                reviewReplyEnabled = reviewReplyEnabled ?: current.reviewReplyEnabled
            )
            next.copy(
                hasChanges = next.messagingEnabled != next.initialMessagingEnabled ||
                    next.messageNotificationsEnabled != next.initialMessageNotificationsEnabled ||
                    next.friendNotificationsEnabled != next.initialFriendNotificationsEnabled ||
                    next.newFollowerEnabled != next.initialNewFollowerEnabled ||
                    next.plateReviewEnabled != next.initialPlateReviewEnabled ||
                    next.reviewReplyEnabled != next.initialReviewReplyEnabled
            )
        }
    }
}
