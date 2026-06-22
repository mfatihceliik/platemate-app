package com.mefy.platemate.presentation.features.main.platedetail

import androidx.lifecycle.SavedStateHandle
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.plate.PlateDetailReview
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlateCodesUseCase
import com.mefy.platemate.domain.usecase.saved.ToggleSavedPlateUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.SearchPlateUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.CityNameResolver
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class PlateDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchPlateUseCase: SearchPlateUseCase,
    private val observeSavedPlateCodesUseCase: ObserveSavedPlateCodesUseCase,
    private val toggleSavedPlateUseCase: ToggleSavedPlateUseCase,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val normalizedPlateCode: String = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow(PlateDetailUiState())
    val uiState: StateFlow<PlateDetailUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<PlateDetailUiEffect>()
    val uiEffect: SharedFlow<PlateDetailUiEffect> = _uiEffect.asSharedFlow()

    private var latestPlate: PlateSearchResult? = null

    init {
        observeBookmark()
        loadPlateDetail()
    }

    fun onAction(action: PlateDetailUiAction) {
        when (action) {
            PlateDetailUiAction.BackClicked -> launch { _uiEffect.emit(PlateDetailUiEffect.NavigateBack) }
            PlateDetailUiAction.BookmarkClicked -> onBookmarkClicked()
            PlateDetailUiAction.ReviewClicked -> launch {
                _uiEffect.emit(PlateDetailUiEffect.NavigateToReview(_uiState.value.plateCode))
            }
            PlateDetailUiAction.RetryClicked -> loadPlateDetail()
        }
    }

    private fun observeBookmark() {
        launch {
            observeSavedPlateCodesUseCase().collectLatest { codes ->
                _uiState.update { it.copy(isBookmarked = codes.contains(normalizedPlateCode)) }
            }
        }
    }

    private fun onBookmarkClicked() {
        val plate = latestPlate ?: return
        launch {
            val savedPlate = SavedPlate(
                normalizedPlateCode = normalizedPlateCode,
                formattedPlateCode = formatTurkishPlateInputUseCase(plate.plateCode),
                cityName = CityNameResolver.resolveCityName(
                    cityName = plate.cityName,
                    plateCode = plate.plateCode
                ),
                ratingAverage = plate.ratingAverage,
                commentCount = plate.reviewCount,
                reportTypes = plate.topReportTypes,
                savedAt = System.currentTimeMillis()
            )
            toggleSavedPlateUseCase(savedPlate)
        }
    }

    private fun loadPlateDetail() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch {
            when (val result = searchPlateUseCase(normalizedPlateCode)) {
                is AppResult.Success -> {
                    val plate = result.data
                    latestPlate = plate

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

                    val reviews = plate.recentReviews.map { it.toUiModel() }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            plateCode = plate.plateCode,
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

    private fun PlateDetailReview.toUiModel(): PlateReviewUiModel {
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
            reportTags = reportTags
        )
    }
}
