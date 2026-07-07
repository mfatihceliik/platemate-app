package com.mefy.platemate.presentation.features.uimodel

enum class ProfileReviewStatusUi {
    APPROVED,
    PENDING_REVIEW,
    REJECTED,
    REMOVED_BY_USER,
    REMOVED_BY_MODERATOR,
    REMOVED_BY_LEGAL_REQUEST,
    UNKNOWN;

    companion object {
        fun from(rawStatus: String): ProfileReviewStatusUi = when (rawStatus.trim().uppercase()) {
            "APPROVED" -> APPROVED
            "PENDING_REVIEW" -> PENDING_REVIEW
            "REJECTED" -> REJECTED
            "REMOVED_BY_USER" -> REMOVED_BY_USER
            "REMOVED_BY_MODERATOR" -> REMOVED_BY_MODERATOR
            "REMOVED_BY_LEGAL_REQUEST" -> REMOVED_BY_LEGAL_REQUEST
            else -> UNKNOWN
        }
    }
}
