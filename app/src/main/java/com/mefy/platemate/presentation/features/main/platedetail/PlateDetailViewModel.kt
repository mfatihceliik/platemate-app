package com.mefy.platemate.presentation.features.main.platedetail

import androidx.lifecycle.SavedStateHandle
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.plate.PlateDetailReview
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.review.ReportReviewUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.uimodel.CommentReportReason
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

@HiltViewModel
class PlateDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchPlateUseCase: SearchPlateUseCase,
    private val reportReviewUseCase: ReportReviewUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

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
            PlateDetailUiAction.MenuClicked -> launch {
                _uiEffect.emit(PlateDetailUiEffect.NavigateToActions(normalizedPlateCode))
            }
            PlateDetailUiAction.ReviewClicked -> launch {
                _uiEffect.emit(PlateDetailUiEffect.NavigateToReview(_uiState.value.plateCode))
            }
            PlateDetailUiAction.RetryClicked -> loadPlateDetail()
            is PlateDetailUiAction.AvatarClicked -> launch {
                _uiEffect.emit(PlateDetailUiEffect.NavigateToUserProfile(action.userId))
            }
            is PlateDetailUiAction.EditReviewClicked -> onEditReviewClicked(action.reviewId)
            is PlateDetailUiAction.ReportReviewClicked -> onReportReviewClicked(action.reviewId)
            is PlateDetailUiAction.ReportReasonSelected -> onReportReasonSelected(action.reason)
            is PlateDetailUiAction.ReportDescriptionChanged -> onReportDescriptionChanged(action.description)
            PlateDetailUiAction.ReportSubmitClicked -> onReportSubmitClicked()
            PlateDetailUiAction.ReportDismissed -> _uiState.update { it.copy(reviewReport = null) }
        }
    }

    private fun onEditReviewClicked(reviewId: Long) {
        val review = _uiState.value.reviews.firstOrNull { it.id == reviewId } ?: return
        launch {
            _uiEffect.emit(
                PlateDetailUiEffect.NavigateToEditReview(
                    plateCode = _uiState.value.plateCode,
                    reviewId = review.id,
                    rating = review.rating,
                    comment = review.comment.orEmpty()
                )
            )
        }
    }

    private fun onReportReviewClicked(reviewId: Long) {
        val review = _uiState.value.reviews.firstOrNull { it.id == reviewId } ?: return
        _uiState.update {
            it.copy(
                reviewReport = ReviewReportUiState(
                    reviewId = review.id,
                    reviewerName = review.displayName ?: review.username,
                    initials = review.initials
                )
            )
        }
    }

    private fun onReportReasonSelected(reason: CommentReportReason) {
        _uiState.update { state ->
            state.reviewReport?.let { state.copy(reviewReport = it.copy(selectedReason = reason)) } ?: state
        }
    }

    private fun onReportDescriptionChanged(description: String) {
        _uiState.update { state ->
            state.reviewReport?.let { state.copy(reviewReport = it.copy(description = description)) } ?: state
        }
    }

    private fun onReportSubmitClicked() {
        val report = _uiState.value.reviewReport ?: return
        val reason = report.selectedReason ?: return
        if (report.isSubmitting) return

        _uiState.update { it.copy(reviewReport = report.copy(isSubmitting = true)) }
        launch {
            when (val result = reportReviewUseCase(report.reviewId, reason.code, report.description)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(reviewReport = null) }
                    showSuccess(UiText.Resource(R.string.report_review_success))
                }
                is AppResult.Error -> {
                    _uiState.update { state ->
                        state.reviewReport?.let { state.copy(reviewReport = it.copy(isSubmitting = false)) } ?: state
                    }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun loadPlateDetail() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch {
            when (val result = searchPlateUseCase(normalizedPlateCode)) {
                is AppResult.Success -> {
                    val plate = result.data

                    val breakdown = plate.ratingDistribution.map { dist ->
                        RatingBreakdownItem(
                            stars = dist.rating,
                            percentage = (dist.percentage / 100.0).toFloat()
                        )
                    }

                    val tags = plate.tagSummary.map { tag ->
                        PlateTagUiModel(
                            code = tag.code,
                            label = tag.label,
                            count = tag.count.toInt()
                        )
                    }

                    val currentUserId = observeSessionUseCase().first()?.userId ?: 0L
                    val reviews = plate.recentReviews.map { it.toUiModel(currentUserId) }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            plateCode = formatTurkishPlateInputUseCase(plate.plateCode),
                            cityCode = plate.plateCode.take(2),
                            cityName = plate.cityName,
                            ratingAverage = plate.ratingAverage,
                            reviewCount = plate.reviewCount,
                            ratingBreakdown = breakdown,
                            tags = tags,
                            reviews = reviews
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
                }
            }
        }
    }

    private fun PlateDetailReview.toUiModel(currentUserId: Long): PlateReviewUiModel {
        val name = displayName ?: username
        val initials = name
            .split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
            .joinToString("")
            .ifEmpty { "?" }

        val timeText = createdAt
            ?.substringBefore("T")
            ?: ""

        return PlateReviewUiModel(
            id = id,
            username = username,
            displayName = displayName,
            initials = initials,
            profilePhotoUrl = profilePhotoUrl,
            rating = rating,
            timeAgo = timeText,
            comment = comment,
            reportTags = reportTags,
            userId = userId,
            canReport = userId != currentUserId,
            canEdit = userId == currentUserId
        )
    }
}
