package com.mefy.platemate.presentation.features.main.search.usersearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.data.local.RecentUserSearchStore
import com.mefy.platemate.domain.usecase.user.SearchUserByUsernameUseCase
import com.mefy.platemate.presentation.features.uimodel.UserSearchItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val searchUserByUsernameUseCase: SearchUserByUsernameUseCase,
    private val recentUserSearchStore: RecentUserSearchStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserSearchUiState())
    val uiState: StateFlow<UserSearchUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UserSearchUiEffect>()
    val uiEffect: SharedFlow<UserSearchUiEffect> = _uiEffect.asSharedFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            recentUserSearchStore.recentSearches.collect { searches ->
                _uiState.update { it.copy(recentSearches = searches) }
            }
        }
    }

    fun onAction(action: UserSearchUiAction) {
        when (action) {
            is UserSearchUiAction.SearchQueryChanged -> onSearchQueryChanged(action.query)
            is UserSearchUiAction.SearchUserClicked -> onSearchUserClicked(action.userId, action.username)
            is UserSearchUiAction.RecentSearchClicked -> onRecentSearchClicked(action.userId, action.username)
            UserSearchUiAction.ClearRecentSearchesClicked -> onClearRecentSearches()
        }
    }

    private fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query, isSearchLoading = true) }
        searchJob?.cancel()
        if (query.trim().length < 3) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearchLoading = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(500)
            when (val result = searchUserByUsernameUseCase(query)) {
                is AppResult.Success -> {
                    val items = result.data.map {
                        UserSearchItemUiModel(
                            id = it.id,
                            username = it.username
                        )
                    }
                    _uiState.update { it.copy(searchResults = items, isSearchLoading = false) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(searchResults = emptyList(), isSearchLoading = false) }
                }
            }
        }
    }

    private fun onSearchUserClicked(userId: Long, username: String) {
        viewModelScope.launch {
            recentUserSearchStore.addSearchQuery(UserSearchItemUiModel(userId, username))
            _uiEffect.emit(UserSearchUiEffect.NavigateToUserProfile(userId))
        }
    }

    private fun onRecentSearchClicked(userId: Long, username: String) {
        viewModelScope.launch {
            // Bring it to the top of recent searches
            recentUserSearchStore.addSearchQuery(UserSearchItemUiModel(userId, username))
            _uiEffect.emit(UserSearchUiEffect.NavigateToUserProfile(userId))
        }
    }

    private fun onClearRecentSearches() {
        viewModelScope.launch {
            recentUserSearchStore.clearSearches()
        }
    }
}
