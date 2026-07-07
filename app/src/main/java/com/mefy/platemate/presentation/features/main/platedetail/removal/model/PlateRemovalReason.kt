package com.mefy.platemate.presentation.features.main.platedetail.removal.model

import androidx.annotation.StringRes
import com.mefy.platemate.R

/**
 * Mirrors the backend [PlateRemovalRequestReason] codes. No list endpoint exists, so the
 * options are fixed here and labels are localized via [labelRes].
 */
enum class PlateRemovalReason(val code: String, @StringRes val labelRes: Int) {
    PLATE_BELONGS_TO_ME("PLATE_BELONGS_TO_ME", R.string.removal_reason_plate_belongs_to_me),
    FALSE_INFORMATION("FALSE_INFORMATION", R.string.removal_reason_false_information),
    PRIVACY_REQUEST("PRIVACY_REQUEST", R.string.removal_reason_privacy_request),
    HARASSMENT("HARASSMENT", R.string.removal_reason_harassment),
    LEGAL_REQUEST("LEGAL_REQUEST", R.string.removal_reason_legal_request),
    OTHER("OTHER", R.string.removal_reason_other)
}
