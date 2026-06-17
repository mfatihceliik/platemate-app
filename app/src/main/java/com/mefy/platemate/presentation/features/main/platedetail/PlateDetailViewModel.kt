package com.mefy.platemate.presentation.features.main.platedetail

import androidx.lifecycle.SavedStateHandle
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.usecase.review.GetPlateReviewsUseCase
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
class PlateDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchPlateUseCase: SearchPlateUseCase,
    private val getPlateReviewsUseCase: GetPlateReviewsUseCase,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val normalizedPlateCode: String = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow(PlateDetailUiState())
    val uiState: StateFlow<PlateDetailUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PlateDetailUiEffect>()
    val uiEffect: SharedFlow<PlateDetailUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadPlateDetail()
    }

    fun onAction(action: PlateDetailUiAction) {
        when (action) {
            PlateDetailUiAction.BackClicked -> launch { _uiEffect.emit(PlateDetailUiEffect.NavigateBack) }
            PlateDetailUiAction.BookmarkClicked -> {}
            PlateDetailUiAction.ReviewClicked -> launch {
                _uiEffect.emit(PlateDetailUiEffect.NavigateToReview(_uiState.value.plateCode))
            }
        }
    }

    private fun loadPlateDetail() {
        launch {
            when (val result = searchPlateUseCase(normalizedPlateCode)) {
                is AppResult.Success -> {
                    val plate = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            plateCode = plate.plateCode,
                            cityCode = plate.plateCode.take(2),
                            cityName = plate.cityName,
                            ratingAverage = plate.ratingAverage,
                            reviewCount = plate.reviewCount
                        )
                    }
                    loadReviews(plate.plateCode)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    handleError(result.error)
                }
            }
        }
    }

    private fun loadReviews(plateCode: String) {
        launch {
            when (val result = getPlateReviewsUseCase(plateCode)) {
                is AppResult.Success -> {
                    val reviews = result.data.items.map { it.toUiModel() }
                    val breakdown = buildBreakdownFromReviews(result.data.items)
                    _uiState.update {
                        it.copy(
                            reviews = reviews,
                            ratingBreakdown = breakdown
                        )
                    }
                }
                is AppResult.Error -> handleError(result.error)
            }
        }
    }

    private fun Review.toUiModel(): PlateReviewUiModel {
        val initials = reviewerUsername
            .split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
            .ifEmpty { "?" }

        val timeText = createdAt?.iso8601
            ?.substringBefore("T")
            ?: ""

        return PlateReviewUiModel(
            id = id,
            username = reviewerUsername,
            initials = initials,
            rating = rating,
            timeAgo = timeText,
            comment = comment
        )
    }

    private fun buildBreakdownFromReviews(reviews: List<Review>): List<RatingBreakdownItem> {
        if (reviews.isEmpty()) return emptyList()
        val total = reviews.size.toFloat()
        return (5 downTo 1).map { star ->
            val count = reviews.count { it.rating == star }
            RatingBreakdownItem(stars = star, percentage = count / total)
        }
    }
}
