package com.mefy.platemate.presentation.features.main.platedetail.removal

import com.mefy.platemate.domain.model.plate.PlateRemovalReason

data class PlateRemovalRequestUiState(
    val plateCode: String = "",
    val reasons: List<PlateRemovalReason> = emptyList(),
    val isLoadingReasons: Boolean = true,
    val selectedReason: PlateRemovalReason? = null,
    val description: String = "",
    val isSubmitting: Boolean = false
) {
    val isSubmitEnabled: Boolean
        get() = selectedReason != null && (!selectedReason.requiresDescription || description.isNotBlank()) && !isSubmitting && !isLoadingReasons

    companion object {
        const val DESCRIPTION_MAX_LENGTH = 1000
    }
}