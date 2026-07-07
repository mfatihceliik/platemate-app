package com.mefy.platemate.presentation.features.uimodel

import androidx.annotation.StringRes
import com.mefy.platemate.R

enum class ReportReason(@StringRes val labelRes: Int, @StringRes val descriptionRes: Int) {
    INAPPROPRIATE_CONTENT(R.string.report_reason_inappropriate_content, R.string.report_reason_inappropriate_content_desc),
    SPAM(R.string.report_reason_spam, R.string.report_reason_spam_desc),
    HARASSMENT(R.string.report_reason_harassment, R.string.report_reason_harassment_desc),
    OTHER(R.string.report_reason_other, R.string.report_reason_other_desc)
}
