package com.mefy.platemate.presentation.features.main.discover

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.discovery.DiscoveryHome
import com.mefy.platemate.domain.model.discovery.DiscoveryTabFilter
import com.mefy.platemate.domain.model.discovery.DiscoveryTabs
import com.mefy.platemate.domain.model.plate.PlateDetail
import com.mefy.platemate.domain.model.settings.PlateCardStyle
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.discovery.GetDiscoveryHomeUseCase
import com.mefy.platemate.domain.usecase.discovery.GetDiscoveryTabFeedUseCase
import com.mefy.platemate.domain.usecase.review.GetReportTypesUseCase
import com.mefy.platemate.domain.usecase.saved.ObserveSavedPlateCodesUseCase
import com.mefy.platemate.domain.usecase.saved.ToggleSavedPlateUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.domain.usecase.settings.ObservePlateCardStyleUseCase
import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.main.discover.mapper.DiscoverUiMapper
import com.mefy.platemate.presentation.features.main.discover.reducer.DiscoverStateReducer
import com.mefy.platemate.presentation.features.uimodel.DiscoverFeedFilterUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverReportTypeOptionUi
import com.mefy.platemate.presentation.features.uimodel.DiscoverTabOptionUiModel
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
class DiscoverViewModel @Inject constructor(
    private val getDiscoveryHomeUseCase: GetDiscoveryHomeUseCase,
    private val getDiscoveryTabFeedUseCase: GetDiscoveryTabFeedUseCase,
    private val getReportTypesUseCase: GetReportTypesUseCase,
    private val observePlateCardStyleUseCase: ObservePlateCardStyleUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val observeSavedPlateCodesUseCase: ObserveSavedPlateCodesUseCase,
    private val toggleSavedPlateUseCase: ToggleSavedPlateUseCase,
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase,
    private val discoverUiMapper: DiscoverUiMapper,
    private val discoverStateReducer: DiscoverStateReducer,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private val _uiEffects = MutableSharedFlow<DiscoverUiEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEffects: SharedFlow<DiscoverUiEffect> = _uiEffects.asSharedFlow()

    private var currentTabs: DiscoveryTabs? = null
    private var currentHome: DiscoveryHome? = null
    private var latestBookmarkedCodes: Set<String> = emptySet()
    private var latestCardStyle: PlateCardStyle = PlateCardStyle.CLASSIC

    private var currentFeedPlates: List<PlateDetail> = emptyList()

    private var seededFromHome: Boolean = false

    init {
        observeBookmarks()
        observeCardStyle()
        observeSession()
        loadDiscoveryHome(forceRefresh = false)
    }

    private fun applyTabOptions(home: DiscoveryHome) {
        val options = home.tabOptions.map {
            DiscoverTabOptionUiModel(code = it.code, label = it.label)
        }
        if (options.isEmpty()) return
        _uiState.update { discoverStateReducer.onTabOptionsLoaded(it, options) }
        val currentCode = _uiState.value.selectedTabCode
        if (options.none { it.code == currentCode }) {
            onFilterSelected(options.first().code)
        }
    }
    private fun observeSession() {
        launch {
            observeSessionUseCase().collectLatest { session ->
                val isPremium = session?.isPremium == true
                _uiState.update { discoverStateReducer.onPremiumChanged(it, isPremium) }
                applyCardStyle()
            }
        }
    }

    private fun observeCardStyle() {
        launch {
            observePlateCardStyleUseCase().collectLatest { style ->
                latestCardStyle = style
                applyCardStyle()
            }
        }
    }

    private fun applyCardStyle() {
        val effectiveStyle = if (_uiState.value.isPremium) latestCardStyle.toUiStyle() else PMPlateCardStyle.Classic
        _uiState.update { discoverStateReducer.onCardStyleChanged(it, effectiveStyle) }
    }

    private fun PlateCardStyle.toUiStyle(): PMPlateCardStyle = when (this) {
        PlateCardStyle.CLASSIC -> PMPlateCardStyle.Classic
        PlateCardStyle.SPOTLIGHT -> PMPlateCardStyle.Spotlight
        PlateCardStyle.MINIMAL -> PMPlateCardStyle.Minimal
    }

    private fun observeBookmarks() {
        launch {
            observeSavedPlateCodesUseCase().collectLatest { codes ->
                latestBookmarkedCodes = codes
                if (currentFeedPlates.isNotEmpty()) {
                    val plateDetails = discoverUiMapper.mapFeedPlates(
                        plates = currentFeedPlates,
                        filter = _uiState.value.selectedTabCode,
                        bookmarkedCodes = codes,
                        startRank = 1
                    )
                    val forYou = currentHome?.let { discoverUiMapper.mapHome(it, codes).forYou }
                    _uiState.update { current ->
                        discoverStateReducer.onBookmarksChanged(current, plateDetails, forYou ?: current.forYou)
                    }
                }
            }
        }
    }

    private fun loadDiscoveryHome(forceRefresh: Boolean) {
        val refreshing = shouldShowRefreshingState(current = _uiState.value, forceRefresh = forceRefresh)
        _uiState.update { current ->
            if (refreshing) {
                discoverStateReducer.onRefreshing(current)
            } else {
                discoverStateReducer.onInitialLoading(current)
            }
        }
        launch {
            when (val result = getDiscoveryHomeUseCase(forceRefresh = forceRefresh)) {
                is AppResult.Success -> {
                    val mappedHome = discoverUiMapper.mapHome(result.data, latestBookmarkedCodes)
                    currentHome = result.data
                    currentTabs = mappedHome.tabs
                    applyTabOptions(result.data)
                    val tabCode = _uiState.value.selectedTabCode
                    val seeded = seedFeedFromHome(tabCode) { plateDetails, endReached ->
                        _uiState.update { current ->
                            discoverStateReducer.onContentLoaded(
                                state = current,
                                mappedHome = mappedHome,
                                plateDetails = plateDetails,
                                endReached = endReached
                            )
                        }
                    }
                    if (!seeded) {
                        _uiState.update { current ->
                            discoverStateReducer.onContentLoaded(
                                state = current,
                                mappedHome = mappedHome,
                                plateDetails = emptyList(),
                                endReached = false
                            )
                        }
                        fetchFeedPage(page = 0, replace = true)
                    }
                    applyCardStyle()
                }
                is AppResult.Error -> {
                    val message = result.error.toUiText()
                    if (refreshing) {
                        _uiState.update { it.copy(isRefreshing = false) }
                        showError(message)
                    } else {
                        _uiState.update { discoverStateReducer.onLoadError(it, message) }
                    }
                }
            }
        }
    }

    fun onAction(action: DiscoverUiAction) {
        when (action) {
            is DiscoverUiAction.FilterSelected -> onFilterSelected(action.tabCode)
            is DiscoverUiAction.TrendPlateClicked -> onTrendPlateClicked(action.trendId)
            is DiscoverUiAction.TrendPlateBookmarkClicked -> onTrendPlateBookmarkClicked(action.trendId)
            is DiscoverUiAction.CityStatClicked -> onCityStatClicked(action.cityId, action.cityName)
            is DiscoverUiAction.RecentActivityPlateClicked -> onRecentActivityPlateClicked(action.plateCode)
            DiscoverUiAction.FilterScreenOpened -> onFilterScreenOpened()
            is DiscoverUiAction.DraftCitiesChanged -> onDraftChanged { it.copy(cityIds = action.cityIds, cityNames = action.cityNames) }
            is DiscoverUiAction.DraftMinRatingChanged -> onDraftChanged { it.copy(minRating = action.minRating.takeIf { r -> r > 0 }) }
            is DiscoverUiAction.DraftReportTypeChanged -> onDraftChanged { it.copy(reportTypeCode = action.code, reportTypeLabel = action.label) }
            is DiscoverUiAction.DraftWindowChanged -> onDraftChanged { it.copy(windowDays = action.windowDays) }
            DiscoverUiAction.FiltersApplied -> onFiltersApplied()
            DiscoverUiAction.FiltersCleared -> onFiltersCleared()
            DiscoverUiAction.LoadMoreRequested -> onLoadMoreRequested()
        }
    }

    override fun onRetry() {
        loadDiscoveryHome(forceRefresh = false)
    }

    override fun onRefresh() {
        val state = _uiState.value
        if (state.isInitialLoading || state.isRefreshing) return
        loadDiscoveryHome(forceRefresh = true)
    }

    private fun onFilterSelected(tabCode: String) {
        if (_uiState.value.activeFilters.hasActiveFilters) {
            _uiState.update { current ->
                discoverStateReducer.onFilterSelected(
                    state = current,
                    tabCode = tabCode,
                    plateDetails = emptyList(),
                    endReached = false
                )
            }
            fetchFeedPage(page = 0, replace = true)
            return
        }

        val seeded = seedFeedFromHome(tabCode) { plateDetails, endReached ->
            _uiState.update { current ->
                discoverStateReducer.onFilterSelected(
                    state = current,
                    tabCode = tabCode,
                    plateDetails = plateDetails,
                    endReached = endReached
                )
            }
        }
        if (!seeded) {
            _uiState.update { current ->
                discoverStateReducer.onFilterSelected(
                    state = current,
                    tabCode = tabCode,
                    plateDetails = emptyList(),
                    endReached = false
                )
            }
            fetchFeedPage(page = 0, replace = true)
        }
    }

    private fun onFilterScreenOpened() {
        _uiState.update { discoverStateReducer.onFilterDraftSeeded(it) }
        val state = _uiState.value
        if (state.isPremium && state.availableReportTypes.isEmpty()) {
            loadReportTypeOptions()
        }
    }

    private fun onDraftChanged(transform: (DiscoverFeedFilterUi) -> DiscoverFeedFilterUi) {
        _uiState.update { discoverStateReducer.onFilterDraftChanged(it, transform(it.filterDraft)) }
    }

    private fun loadReportTypeOptions() {
        launch {
            when (val result = getReportTypesUseCase()) {
                is AppResult.Success -> {
                    val options = result.data.map { reportType ->
                        DiscoverReportTypeOptionUi(
                            code = reportType.code,
                            label = reportType.label,
                            colorHex = reportType.colorHex
                        )
                    }
                    _uiState.update { discoverStateReducer.onReportTypeOptionsLoaded(it, options) }
                }
                is AppResult.Error -> {
                }
            }
        }
    }

    private fun onFiltersApplied() {
        val draft = _uiState.value.filterDraft
        if (!draft.hasActiveFilters) {
            onFiltersCleared()
            return
        }
        _uiState.update { discoverStateReducer.onFiltersApplied(it, draft) }
        fetchFeedPage(page = 0, replace = true)
    }

    private fun onFiltersCleared() {
        _uiState.update { discoverStateReducer.onFiltersApplied(it, DiscoverFeedFilterUi()) }
        val tabCode = _uiState.value.selectedTabCode
        val seeded = seedFeedFromHome(tabCode) { plateDetails, endReached ->
            _uiState.update { current ->
                discoverStateReducer.onFilterSelected(
                    state = current,
                    tabCode = tabCode,
                    plateDetails = plateDetails,
                    endReached = endReached
                )
            }
        }
        if (!seeded) {
            _uiState.update { current ->
                discoverStateReducer.onFilterSelected(
                    state = current,
                    tabCode = tabCode,
                    plateDetails = emptyList(),
                    endReached = false
                )
            }
            fetchFeedPage(page = 0, replace = true)
        }
    }

    private fun onLoadMoreRequested() {
        val state = _uiState.value
        if (!state.activeFilters.hasActiveFilters) return
        if (state.plateDetail.isEmpty()) return
        if (state.isInitialLoading || state.isRefreshing || state.isLoadingMore || state.endReached) return

        _uiState.update { discoverStateReducer.onLoadMoreStarted(it) }
        if (seededFromHome) {
            fetchFeedPage(page = 0, replace = true)
        } else {
            fetchFeedPage(page = state.page + 1, replace = false)
        }
    }

    private fun seedFeedFromHome(
        tabCode: String,
        onSeeded: (List<com.mefy.platemate.presentation.features.uimodel.PlateDetailUiModel>, Boolean) -> Unit
    ): Boolean {
        val tabs = currentTabs ?: return false
        val tabPlates = tabPlatesFor(tabs, tabCode) ?: return false
        currentFeedPlates = tabPlates
        seededFromHome = true
        val plateDetails = discoverUiMapper.mapFeedPlates(
            plates = tabPlates,
            filter = tabCode,
            bookmarkedCodes = latestBookmarkedCodes,
            startRank = 1
        )
        onSeeded(plateDetails, tabPlates.size < HOME_TAB_LIMIT)
        return true
    }

    private fun fetchFeedPage(page: Int, replace: Boolean) {
        launch {
            val state = _uiState.value
            val result = getDiscoveryTabFeedUseCase(
                tab = state.selectedTabCode,
                filter = state.activeFilters.toDomainFilter(),
                page = page,
                size = PAGE_SIZE
            )
            when (result) {
                is AppResult.Success -> {
                    val mergedPlates = if (replace) {
                        result.data.items
                    } else {
                        currentFeedPlates + result.data.items
                    }
                    currentFeedPlates = mergedPlates
                    seededFromHome = false
                    val plateDetails = discoverUiMapper.mapFeedPlates(
                        plates = mergedPlates,
                        filter = _uiState.value.selectedTabCode,
                        bookmarkedCodes = latestBookmarkedCodes,
                        startRank = 1
                    )
                    _uiState.update { current ->
                        discoverStateReducer.onPageLoaded(
                            state = current,
                            plateDetails = plateDetails,
                            page = result.data.page,
                            hasNext = result.data.hasNext
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { discoverStateReducer.onLoadMoreFailed(it) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    private fun onTrendPlateClicked(trendId: String) {
        val plateCode = trendId.substringBefore("_")
        val normalizedCode = validateTurkishPlateUseCase.normalize(plateCode)
        launch {
            _uiEffects.emit(DiscoverUiEffect.NavigateToTrendDetail(normalizedCode))
        }
    }

    private fun onTrendPlateBookmarkClicked(trendId: String) {
        launch {
            val plateCode = trendId.substringBefore("_")
            val plateDetail = currentFeedPlates.firstOrNull { it.plateCode == plateCode }
                ?: currentTabs?.let { tabs ->
                    (tabs.trendPlates + tabs.attentionPlates + tabs.goodDriverPlates + tabs.newPlates)
                        .firstOrNull { it.plateCode == plateCode }
                }
                ?: return@launch

            val formattedCode = formatTurkishPlateInputUseCase(plateDetail.plateCode)
            val normalizedCode = validateTurkishPlateUseCase.normalize(plateDetail.plateCode)

            val savedPlate = com.mefy.platemate.domain.model.search.SavedPlate(
                normalizedPlateCode = normalizedCode,
                formattedPlateCode = formattedCode,
                cityName = com.mefy.platemate.presentation.common.text.CityNameResolver.resolveCityName(
                    cityName = plateDetail.cityName,
                    plateCode = plateDetail.plateCode
                ),
                ratingAverage = plateDetail.ratingAverage,
                commentCount = plateDetail.reviewCount,
                reportTypes = plateDetail.topReportType,
                savedAt = System.currentTimeMillis()
            )

            toggleSavedPlateUseCase(savedPlate)
        }
    }

    private fun onCityStatClicked(cityId: Int, cityName: String) {
        launch {
            _uiEffects.emit(DiscoverUiEffect.NavigateToCityPlates(cityId = cityId, cityName = cityName))
        }
    }

    private fun onRecentActivityPlateClicked(plateCode: String) {
        val normalizedCode = validateTurkishPlateUseCase.normalize(plateCode)
        launch {
            _uiEffects.emit(DiscoverUiEffect.NavigateToTrendDetail(normalizedCode))
        }
    }



    private fun shouldShowRefreshingState(
        current: DiscoverUiState,
        forceRefresh: Boolean
    ): Boolean {
        if (!forceRefresh) return false
        return currentTabs != null || current.metrics.isNotEmpty() || current.plateDetail.isNotEmpty()
    }

    private fun tabPlatesFor(tabs: DiscoveryTabs, tabCode: String): List<PlateDetail>? =
        when (tabCode) {
            "TREND" -> tabs.trendPlates
            "DANGEROUS" -> tabs.attentionPlates
            "GOOD_DRIVER" -> tabs.goodDriverPlates
            "NEW" -> tabs.newPlates
            else -> null
        }

    private fun DiscoverFeedFilterUi.toDomainFilter(): DiscoveryTabFilter =
        DiscoveryTabFilter(
            cityIds = cityIds,
            minRating = minRating?.toDouble(),
            reportTypeCode = reportTypeCode,
            windowDays = windowDays
        )

    companion object {
        private const val HOME_TAB_LIMIT = 8
        private const val PAGE_SIZE = 20
    }
}
