package com.mefy.platemate.presentation.features.main.settings.notifications

import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.domain.model.settings.UserSettings
import com.mefy.platemate.domain.usecase.settings.GetSettingsUseCase
import com.mefy.platemate.domain.usecase.settings.UpdateSettingsUseCase
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
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
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

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

            is NotificationPreferencesUiAction.OnlineVisibilityChanged ->
                _uiState.updateWith(onlineVisibilityEnabled = action.enabled)

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

            is NotificationPreferencesUiAction.FollowingListVisibleChanged ->
                _uiState.updateWith(followingListVisible = action.enabled)

            NotificationPreferencesUiAction.SaveClicked -> save()
        }
    }

    override fun onRetry() {
        load()
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = {
            _uiState.update { state -> state.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(it)
        }) {
            when (val result = getSettingsUseCase()) {
                is AppResult.Success -> {
                    val settings = result.data.userSettings
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            messagingEnabled = settings.messagingEnabled,
                            onlineVisibilityEnabled = settings.onlineVisibilityEnabled,
                            messageNotificationsEnabled = settings.messageNotificationsEnabled,
                            friendNotificationsEnabled = settings.friendNotificationsEnabled,
                            plateReviewEnabled = settings.plateReviewNotificationsEnabled,
                            newFollowerEnabled = settings.newFollowerNotificationsEnabled,
                            reviewReplyEnabled = settings.reviewReplyNotificationsEnabled,
                            followingListVisible = settings.followingListVisible,
                            initialMessagingEnabled = settings.messagingEnabled,
                            initialOnlineVisibilityEnabled = settings.onlineVisibilityEnabled,
                            initialMessageNotificationsEnabled = settings.messageNotificationsEnabled,
                            initialFriendNotificationsEnabled = settings.friendNotificationsEnabled,
                            initialPlateReviewEnabled = settings.plateReviewNotificationsEnabled,
                            initialNewFollowerEnabled = settings.newFollowerNotificationsEnabled,
                            initialReviewReplyEnabled = settings.reviewReplyNotificationsEnabled,
                            initialFollowingListVisible = settings.followingListVisible,
                            hasChanges = false
                        )
                    }
                }

                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
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
                        onlineVisibilityEnabled = state.onlineVisibilityEnabled,
                        messageNotificationsEnabled = state.messageNotificationsEnabled,
                        friendNotificationsEnabled = state.friendNotificationsEnabled,
                        plateReviewNotificationsEnabled = state.plateReviewEnabled,
                        newFollowerNotificationsEnabled = state.newFollowerEnabled,
                        reviewReplyNotificationsEnabled = state.reviewReplyEnabled,
                        followingListVisible = state.followingListVisible
                    )
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            initialMessagingEnabled = it.messagingEnabled,
                            initialOnlineVisibilityEnabled = it.onlineVisibilityEnabled,
                            initialMessageNotificationsEnabled = it.messageNotificationsEnabled,
                            initialFriendNotificationsEnabled = it.friendNotificationsEnabled,
                            initialNewFollowerEnabled = it.newFollowerEnabled,
                            initialPlateReviewEnabled = it.plateReviewEnabled,
                            initialReviewReplyEnabled = it.reviewReplyEnabled,
                            initialFollowingListVisible = it.followingListVisible,
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
                    handleError(result.error)
                    _uiState.update { it.copy(isSaving = false) }
                }
            }
        }
    }

    private fun MutableStateFlow<NotificationPreferencesUiState>.updateWith(
        messagingEnabled: Boolean? = null,
        onlineVisibilityEnabled: Boolean? = null,
        messageNotificationsEnabled: Boolean? = null,
        friendNotificationsEnabled: Boolean? = null,
        newFollowerEnabled: Boolean? = null,
        plateReviewEnabled: Boolean? = null,
        reviewReplyEnabled: Boolean? = null,
        followingListVisible: Boolean? = null
    ) {
        update { current ->
            val next = current.copy(
                messagingEnabled = messagingEnabled ?: current.messagingEnabled,
                onlineVisibilityEnabled = onlineVisibilityEnabled ?: current.onlineVisibilityEnabled,
                messageNotificationsEnabled = messageNotificationsEnabled ?: current.messageNotificationsEnabled,
                friendNotificationsEnabled = friendNotificationsEnabled ?: current.friendNotificationsEnabled,
                newFollowerEnabled = newFollowerEnabled ?: current.newFollowerEnabled,
                plateReviewEnabled = plateReviewEnabled ?: current.plateReviewEnabled,
                reviewReplyEnabled = reviewReplyEnabled ?: current.reviewReplyEnabled,
                followingListVisible = followingListVisible ?: current.followingListVisible
            )
            next.copy(
                hasChanges = next.messagingEnabled != next.initialMessagingEnabled ||
                    next.onlineVisibilityEnabled != next.initialOnlineVisibilityEnabled ||
                    next.messageNotificationsEnabled != next.initialMessageNotificationsEnabled ||
                    next.friendNotificationsEnabled != next.initialFriendNotificationsEnabled ||
                    next.newFollowerEnabled != next.initialNewFollowerEnabled ||
                    next.plateReviewEnabled != next.initialPlateReviewEnabled ||
                    next.reviewReplyEnabled != next.initialReviewReplyEnabled ||
                    next.followingListVisible != next.initialFollowingListVisible
            )
        }
    }
}
