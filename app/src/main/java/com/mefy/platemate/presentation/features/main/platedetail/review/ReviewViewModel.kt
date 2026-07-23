package com.mefy.platemate.presentation.features.main.platedetail.review

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.usecase.review.AddPlateReviewUseCase
import com.mefy.platemate.domain.usecase.review.GetReportTypesUseCase
import com.mefy.platemate.domain.usecase.review.GetReviewByIdUseCase
import com.mefy.platemate.domain.usecase.review.UpdatePlateReviewUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.presentation.navigation.ReviewDestination
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewUiState.Companion.REVIEW_COMMENT_MAX_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
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
    private val getReviewByIdUseCase: GetReviewByIdUseCase,
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
            isEditMode = isEditMode
        )
    )
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ReviewUiEffect>()
    val uiEffect: SharedFlow<ReviewUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadData()
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

    private fun loadData() {
        launch {
            val plateInfoDeferred = async { searchPlateUseCase(plateCode) }
            val reportTypesDeferred = async { getReportTypesUseCase() }
            val reviewDeferred = if (isEditMode) async { getReviewByIdUseCase(editReviewId) } else null

            var reportTags = emptyList<ReviewTagUiModel>()
            var initialRating = 0
            var initialComment = ""
            var preSelectedCodes = emptyList<String>()

            when (val typesResult = reportTypesDeferred.await()) {
                is AppResult.Success -> {
                    reportTags = typesResult.data
                        .sortedBy { it.sortOrder }
                        .map { ReviewTagUiModel(code = it.code, label = it.label) }
                }
                is AppResult.Error -> {}
            }

            if (reviewDeferred != null) {
                when (val reviewResult = reviewDeferred.await()) {
                    is AppResult.Success -> {
                        initialRating = reviewResult.data.rating
                        initialComment = reviewResult.data.comment
                        preSelectedCodes = reviewResult.data.reportTypeCodes
                    }
                    is AppResult.Error -> {
                        handleError(reviewResult.error)
                    }
                }
            }

            // Update tags with pre-selected codes
            val safePreSelectedCodes = preSelectedCodes.map { it.trim().uppercase() }
            val updatedTags = reportTags.map { tag ->
                if (safePreSelectedCodes.contains(tag.code.trim().uppercase())) tag.copy(isSelected = true) else tag
            }

            when (val plateResult = plateInfoDeferred.await()) {
                is AppResult.Success -> {
                    val plate = plateResult.data
                    _uiState.update {
                        it.copy(
                            cityCode = plate.plateCode.take(2),
                            cityName = plate.cityName,
                            reviewCount = plate.reviewCount,
                            isLoading = false,
                            tags = updatedTags,
                            overallRating = initialRating,
                            comment = initialComment
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    handleError(plateResult.error)
                }
            }
        }
    }

    private fun toggleTag(code: String) {
        val state = _uiState.value
        val target = state.tags.firstOrNull { it.code == code } ?: return
        if (!target.isSelected && state.tags.count { it.isSelected } >= MAX_SELECTED_TAGS) {
            showInfo(UiText.Resource(R.string.review_tags_limit_reached, listOf(MAX_SELECTED_TAGS)))
            return
        }
        _uiState.update {
            it.copy(
                tags = it.tags.map { tag ->
                    if (tag.code == code) tag.copy(isSelected = !tag.isSelected) else tag
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
            val selectedCodes = state.tags
                .filter { it.isSelected }
                .map { it.code }
                .ifEmpty { null }
                
            when (val result = updatePlateReviewUseCase(editReviewId, state.overallRating, comment, selectedCodes)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isSubmitting = false, submitResult = ReviewSubmitResult.Success)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isSubmitting = false, submitResult = ReviewSubmitResult.Error(result.error.toUiText()))
                }
            }
        }
    }

    private companion object {
        const val MAX_SELECTED_TAGS = 5
    }
}
