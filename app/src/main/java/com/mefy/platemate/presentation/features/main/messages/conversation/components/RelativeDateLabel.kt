package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun rememberRelativeDateLabel(isoDate: String): String {
    val locale: Locale = LocalConfiguration.current.locales[0]
    val today = stringResource(R.string.conversation_date_today)
    val yesterday = stringResource(R.string.conversation_date_yesterday)
    val lastWeekdayFormat = stringResource(R.string.conversation_date_last_weekday)
    return remember(isoDate, locale, today, yesterday, lastWeekdayFormat) {
        relativeDateLabel(isoDate, locale, today, yesterday, lastWeekdayFormat)
    }
}

private fun relativeDateLabel(
    isoDate: String,
    locale: Locale,
    today: String,
    yesterday: String,
    lastWeekdayFormat: String
): String {
    val parts = isoDate.split("-")
    if (parts.size != 3) return isoDate
    val year = parts[0].toIntOrNull() ?: return isoDate
    val month = parts[1].toIntOrNull() ?: return isoDate
    val day = parts[2].toIntOrNull() ?: return isoDate

    // Compare at noon so daylight-saving transitions can't shift the day diff by one.
    val target = Calendar.getInstance().apply {
        clear()
        set(year, month - 1, day, 12, 0, 0)
    }
    val todayCal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 12)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val diffDays = TimeUnit.MILLISECONDS.toDays(todayCal.timeInMillis - target.timeInMillis)

    return when {
        diffDays == 0L -> today
        diffDays == 1L -> yesterday
        diffDays in 2L..6L -> weekdayName(target, locale)
        diffDays in 7L..13L -> String.format(locale, lastWeekdayFormat, weekdayName(target, locale))
        else -> DateFormat.getDateInstance(DateFormat.LONG, locale).format(Date(target.timeInMillis))
    }
}

private fun weekdayName(cal: Calendar, locale: Locale): String =
    cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale).orEmpty()
