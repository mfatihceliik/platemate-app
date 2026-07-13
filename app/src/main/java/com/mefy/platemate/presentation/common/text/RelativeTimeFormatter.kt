package com.mefy.platemate.presentation.common.text

import com.mefy.platemate.R
import java.time.LocalDateTime
import java.time.ZoneId

object RelativeTimeFormatter {

    private const val MINUTES_IN_HOUR = 60L
    private const val MINUTES_IN_DAY = 60L * 24L
    private const val MINUTES_IN_WEEK = MINUTES_IN_DAY * 7L

    fun format(
        isoDateTime: String?,
        nowMillis: Long = System.currentTimeMillis()
    ): UiText {
        if (isoDateTime.isNullOrBlank()) return UiText.Dynamic("")
        val parsed = runCatching { LocalDateTime.parse(isoDateTime) }.getOrNull()
            ?: return UiText.Dynamic(isoDateTime.substringBefore("T"))

        val eventMillis = parsed.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val elapsedMinutes = (nowMillis - eventMillis) / 60_000L
        return when {
            elapsedMinutes < 1L -> UiText.Resource(R.string.time_ago_just_now)
            elapsedMinutes < MINUTES_IN_HOUR -> UiText.Resource(R.string.time_ago_minutes, listOf(elapsedMinutes))
            elapsedMinutes < MINUTES_IN_DAY -> UiText.Resource(R.string.time_ago_hours, listOf(elapsedMinutes / MINUTES_IN_HOUR))
            elapsedMinutes < MINUTES_IN_WEEK -> UiText.Resource(R.string.time_ago_days, listOf(elapsedMinutes / MINUTES_IN_DAY))
            else -> UiText.Dynamic(isoDateTime.substringBefore("T"))
        }
    }
}
