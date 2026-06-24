package com.mefy.platemate.presentation.features.main.profile.reducer

import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.main.profile.ProfileUiState
import com.mefy.platemate.presentation.features.main.profile.mapper.ProfileUiData
import javax.inject.Inject

class ProfileStateReducer @Inject constructor() {

    fun onInitialLoading(state: ProfileUiState): ProfileUiState = state.copy(
        isInitialLoading = true,
        errorMessage = null
    )

    fun onRefreshing(state: ProfileUiState): ProfileUiState = state.copy(
        isRefreshing = true
    )

    fun onProfileLoaded(
        state: ProfileUiState,
        uiData: ProfileUiData
    ): ProfileUiState = state.copy(
        isInitialLoading = false,
        isRefreshing = false,
        errorMessage = null,
        header = uiData.header,
        accountSummary = uiData.accountSummary,
        stats = uiData.stats,
        statusSummary = uiData.statusSummary,
        socialLinks = uiData.socialLinks,
        activities = uiData.activities
    )

    fun onLoadFailed(state: ProfileUiState, message: UiText): ProfileUiState = state.copy(
        isInitialLoading = false,
        isRefreshing = false,
        errorMessage = message
    )

    fun onRefreshFailed(state: ProfileUiState): ProfileUiState = state.copy(
        isRefreshing = false
    )
}
