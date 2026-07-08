package com.mefy.platemate.presentation.features.main.platedetail.review

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.review.AddPlateReviewUseCase
import com.mefy.platemate.domain.usecase.review.GetReportTypesUseCase
import com.mefy.platemate.domain.usecase.review.UpdatePlateReviewUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.presentation.navigation.ReviewDestination
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewUiState.Companion.REVIEW_COMMENT_MAX_LENGTH
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
    private val updatePlateReviewUseCase: UpdatePlateReviewUseCase,
    private val getReportTypesUseCase: GetReportTypesUseCase,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: ReviewDestination = savedStateHandle.toRoute()
    private val plateCode: String = route.plateCode
    private val editReviewId: Long = route.reviewId
    private val isEditMode: Boolean = editReviewId > 0

    private val _uiState = MutableStateFlow(
        ReviewUiState(
            plateCode = formatTurkishPlateInputUseCase(plateCode),
            isEditMode = isEditMode,
            overallRating = if (isEditMode) route.initialRating else 0,
            comment = if (isEditMode) route.initialComment else ""
        )
    )
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ReviewUiEffect>()
    val uiEffect: SharedFlow<ReviewUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadPlateInfo()
        loadReportTypes()
    }

    fun onAction(action: ReviewUiAction) {
        when (action) {
            ReviewUiAction.BackClicked -> launch { _uiEffect.emit(ReviewUiEffect.NavigateBack) }
            is ReviewUiAction.OverallRatingChanged -> _uiState.update { it.copy(overallRating = action.rating) }
            is ReviewUiAction.TagToggled -> toggleTag(action.code)
            is ReviewUiAction.CommentChanged -> {
                if (action.text.length <= REVIEW_COMMENT_MAX_LENGTH) {
                    _uiState.update { it.copy(comment = action.text) }
                }
            }
            is ReviewUiAction.AnonymousToggled -> _uiState.update { it.copy(isAnonymous = action.checked) }
            ReviewUiAction.SubmitClicked -> submitReview()
            ReviewUiAction.RetrySubmitClicked -> {
                _uiState.update { it.copy(submitResult = null) }
                submitReview()
            }
            ReviewUiAction.GoHomeClicked -> launch { _uiEffect.emit(ReviewUiEffect.ReviewSubmitted) }
            ReviewUiAction.RateAnotherClicked -> _uiState.update {
                it.copy(
                    submitResult = null,
                    overallRating = 0,
                    comment = "",
                    tags = it.tags.map { tag -> tag.copy(isSelected = false) }
                )
            }
            ReviewUiAction.DismissResult -> _uiState.update { it.copy(submitResult = null) }
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
                            cityName = plate.cityName,
                            reviewCount = plate.reviewCount,
                            isLoading = false
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    handleError(result.error)
                }
            }
        }
    }

    private fun loadReportTypes() {
        launch {
            when (val result = getReportTypesUseCase()) {
                is AppResult.Success -> {
                    val tags = result.data
                        .sortedBy { it.sortOrder }
                        .map { ReviewTagUiModel(code = it.code, label = it.label) }
                    _uiState.update { it.copy(tags = tags) }
                }
                is AppResult.Error -> { }
            }
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

        if (isEditMode) {
            submitEdit(state)
            return
        }

        launch {
            val comment = state.comment.ifBlank { null }
            val selectedCodes = state.tags
                .filter { it.isSelected }
                .map { it.code }
                .ifEmpty { null }
            when (val result = addPlateReviewUseCase(plateCode, state.overallRating, comment, selectedCodes)) {
                is AppResult.Success -> {
                    // APPROVED → yayınlandı; değilse (PENDING_REVIEW vb.) moderasyonda.
                    val approved = result.data.status.equals("APPROVED", ignoreCase = true)
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            submitResult = if (approved) ReviewSubmitResult.Success else ReviewSubmitResult.Pending
                        )
                    }
                }
                is AppResult.Error -> {
                    // Hata mesajı ekranın kendi sonuç popup'ında gösterilir (global kanal değil).
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            submitResult = ReviewSubmitResult.Error(result.error.toUiText())
                        )
                    }
                }
            }
        }
    }

    private fun submitEdit(state: ReviewUiState) {
        launch {
            val comment = state.comment.ifBlank { null }
            when (val result = updatePlateReviewUseCase(editReviewId, state.overallRating, comment)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isSubmitting = false, submitResult = ReviewSubmitResult.Success)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, submitResult = ReviewSubmitResult.Error(result.error.toUiText()))
                }
            }
        }
    }
}
