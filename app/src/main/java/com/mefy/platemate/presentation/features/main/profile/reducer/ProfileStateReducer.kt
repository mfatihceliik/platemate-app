package com.mefy.platemate.presentation.features.main.profile.reducer

import com.mefy.platemate.presentation.features.main.profile.ProfileUiState
import com.mefy.platemate.presentation.features.main.profile.mapper.ProfileUiData
import javax.inject.Inject

class ProfileStateReducer @Inject constructor() {

    fun onInitialLoading(state: ProfileUiState): ProfileUiState = state.copy(
        isInitialLoading = true
    )

    fun onProfileLoaded(
        state: ProfileUiState,
        uiData: ProfileUiData
    ): ProfileUiState = state.copy(
        isInitialLoading = false,
        header = uiData.header,
        accountSummary = uiData.accountSummary,
        stats = uiData.stats,
        statusSummary = uiData.statusSummary,
        socialLinks = uiData.socialLinks,
        activities = uiData.activities
    )

    fun onLoadFailed(state: ProfileUiState): ProfileUiState = state.copy(
        isInitialLoading = false
    )
}
