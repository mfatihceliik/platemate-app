package com.mefy.platemate.presentation.features.main.profile.reviewlist

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.usecase.review.GetMyReviewsUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.ProfileReviewListDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val REVIEW_STATUS_CODES = listOf("APPROVED", "PENDING_REVIEW", "REJECTED")
private const val SEARCH_DEBOUNCE_MS = 400L

@HiltViewModel
class ProfileReviewListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMyReviewsUseCase: GetMyReviewsUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: ProfileReviewListDestination = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow(
        ProfileReviewListUiState(
            selectedTab = REVIEW_STATUS_CODES.indexOf(route.initialStatus).coerceAtLeast(0)
        )
    )
    val uiState: StateFlow<ProfileReviewListUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProfileReviewListUiEffect>()
    val uiEffect: SharedFlow<ProfileReviewListUiEffect> = _uiEffect.asSharedFlow()

    private var searchJob: Job? = null

    init {
        loadFirstPage()
    }

    fun onAction(action: ProfileReviewListUiAction) {
        when (action) {
            is ProfileReviewListUiAction.TabChanged -> onTabChanged(action.index)
            is ProfileReviewListUiAction.SearchQueryChanged -> onSearchQueryChanged(action.query)
            ProfileReviewListUiAction.LoadMoreRequested -> loadNextPage()
            is ProfileReviewListUiAction.ReviewClicked -> sendEffect(
                ProfileReviewListUiEffect.NavigateToReviewDetail(action.plateCode, action.reviewId)
            )
            ProfileReviewListUiAction.RetryClicked -> loadFirstPage()
            ProfileReviewListUiAction.BackClicked -> sendEffect(ProfileReviewListUiEffect.NavigateBack)
        }
    }

    private fun currentStatusCode(): String = REVIEW_STATUS_CODES[_uiState.value.selectedTab]

    private fun onTabChanged(index: Int) {
        if (_uiState.value.selectedTab == index) return
        searchJob?.cancel()
        _uiState.update { it.copy(selectedTab = index, reviews = emptyList(), page = 0, endReached = false) }
        loadFirstPage()
    }

    private fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = launch {
            delay(SEARCH_DEBOUNCE_MS)
            _uiState.update { it.copy(reviews = emptyList(), page = 0, endReached = false) }
            loadFirstPage()
        }
    }

    private fun loadFirstPage() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = ::handleError) {
            val query = _uiState.value.searchQuery
            when (val result = getMyReviewsUseCase(status = currentStatusCode(), query = query, page = 0)) {
                is AppResult.Success -> _uiState.update { state ->
                    state.copy(
                        isInitialLoading = false,
                        isLoading = false,
                        errorMessage = null,
                        reviews = result.data.items.map { it.toUiModel() },
                        page = 0,
                        endReached = !result.data.meta.hasNext
                    )
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isInitialLoading = false, isLoading = false, errorMessage = result.error.toUiText())
                }
            }
        }
    }

    private fun loadNextPage() {
        val current = _uiState.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return

        _uiState.update { it.copy(isLoadingMore = true) }
        launch(onError = ::handleError) {
            val nextPage = current.page + 1
            when (val result = getMyReviewsUseCase(status = currentStatusCode(), query = current.searchQuery, page = nextPage)) {
                is AppResult.Success -> _uiState.update { state ->
                    state.copy(
                        isLoadingMore = false,
                        reviews = state.reviews + result.data.items.map { it.toUiModel() },
                        page = nextPage,
                        endReached = !result.data.meta.hasNext
                    )
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun Review.toUiModel(): ProfileReviewListItemUiModel = ProfileReviewListItemUiModel(
        id = id,
        plateCode = plateCode,
        comment = comment,
        rating = rating,
        dateText = createdAt?.iso8601?.take(10).orEmpty()
    )

    private fun sendEffect(effect: ProfileReviewListUiEffect) {
        launch { _uiEffect.emit(effect) }
    }
}
