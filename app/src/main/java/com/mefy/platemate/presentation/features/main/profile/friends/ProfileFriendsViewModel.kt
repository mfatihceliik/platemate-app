package com.mefy.platemate.presentation.features.main.profile.friends

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.social.GetFriendsUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileFriendsViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(ProfileFriendsUiState())
    val uiState: StateFlow<ProfileFriendsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = ::handleError) {
            when (val result = getFriendsUseCase()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            friends = result.data.map { friend ->
                                ProfileFriendUiModel(
                                    id = friend.id,
                                    username = friend.friendUsername,
                                    status = friend.status.name,
                                    createdAtText = friend.createdAt?.iso8601?.take(10).orEmpty().ifBlank { "-" }
                                )
                            }
                        )
                    }
                }

                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
                }
            }
        }
    }
}
