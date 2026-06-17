package com.mefy.platemate.presentation.features.main.review

import androidx.lifecycle.SavedStateHandle
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.usecase.review.AddPlateReviewUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.presentation.common.error.UiErrorResolver
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
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchPlateUseCase: SearchPlateUseCase,
    private val addPlateReviewUseCase: AddPlateReviewUseCase,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val plateCode: String = checkNotNull(savedStateHandle["plateCode"])

    private val _uiState = MutableStateFlow(ReviewUiState(plateCode = plateCode))
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ReviewUiEffect>()
    val uiEffect: SharedFlow<ReviewUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadPlateInfo()
    }

    fun onAction(action: ReviewUiAction) {
        when (action) {
            ReviewUiAction.BackClicked -> launch { _uiEffect.emit(ReviewUiEffect.NavigateBack) }
            is ReviewUiAction.OverallRatingChanged -> _uiState.update { it.copy(overallRating = action.rating) }
            is ReviewUiAction.SubRatingChanged -> updateSubRating(action.key, action.rating)
            is ReviewUiAction.TagToggled -> toggleTag(action.code)
            is ReviewUiAction.CommentChanged -> {
                if (action.text.length <= MAX_COMMENT_LENGTH) {
                    _uiState.update { it.copy(comment = action.text) }
                }
            }
            is ReviewUiAction.AnonymousToggled -> _uiState.update { it.copy(isAnonymous = action.checked) }
            ReviewUiAction.SubmitClicked -> submitReview()
        }
    }

    private fun loadPlateInfo() {
        launch {
            when (val result = searchPlateUseCase(plateCode)) {
                is AppResult.Success -> {
                    val plate = result.data
                    _uiState.update {
                        it.copy(
                            cityCode = plate.plateCode.take(2),
                            cityName = plate.cityName ?: "",
                            reviewCount = plate.reviewCount
                        )
                    }
                }
                is AppResult.Error -> handleError(result.error)
            }
        }
    }

    private fun updateSubRating(key: String, rating: Int) {
        _uiState.update { state ->
            state.copy(
                subRatings = state.subRatings.map {
                    if (it.key == key) it.copy(rating = rating) else it
                }
            )
        }
    }

    private fun toggleTag(code: String) {
        _uiState.update { state ->
            state.copy(
                tags = state.tags.map {
                    if (it.code == code) it.copy(isSelected = !it.isSelected) else it
                }
            )
        }
    }

    private fun submitReview() {
        val state = _uiState.value
        if (!state.isSubmitEnabled) return

        _uiState.update { it.copy(isSubmitting = true) }

        launch {
            val comment = state.comment.ifBlank { null }
            when (val result = addPlateReviewUseCase(plateCode, state.overallRating, comment)) {
                is AppResult.Success -> _uiEffect.emit(ReviewUiEffect.ReviewSubmitted)
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSubmitting = false) }
                    handleError(result.error)
                }
            }
        }
    }

    private companion object {
        const val MAX_COMMENT_LENGTH = 240
    }
}
