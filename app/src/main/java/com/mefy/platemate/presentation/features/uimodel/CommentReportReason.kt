package com.mefy.platemate.presentation.features.uimodel

import androidx.annotation.StringRes
import com.mefy.platemate.R

/**
 * Mirrors the backend [CommentReportReason] codes. No list endpoint exists, so the
 * options are fixed here and labels are localized via [labelRes].
 */
enum class CommentReportReason(val code: String, @StringRes val labelRes: Int) {
    HATE_SPEECH("HATE_SPEECH", R.string.comment_report_reason_hate_speech),
    INSULT("INSULT", R.string.comment_report_reason_insult),
    FALSE_INFORMATION("FALSE_INFORMATION", R.string.comment_report_reason_false_information),
    PERSONAL_DATA("PERSONAL_DATA", R.string.comment_report_reason_personal_data),
    THREAT("THREAT", R.string.comment_report_reason_threat),
    SPAM("SPAM", R.string.comment_report_reason_spam),
    OTHER("OTHER", R.string.comment_report_reason_other)
}