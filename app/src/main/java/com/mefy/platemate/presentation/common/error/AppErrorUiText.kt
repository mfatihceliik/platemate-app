package com.mefy.platemate.presentation.common.error

import com.mefy.platemate.R
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.presentation.common.text.UiText

/**
 * [AppError] -> kullanıcıya gösterilecek metin. Tek eşleme noktası.
 *
 * `Api` backend mesajını olduğu gibi gösterir (LanguageInterceptor sayesinde
 * yerelleştirilmiş gelir); boşsa genel mesaja düşer.
 */
fun AppError.toUiText(): UiText = when (this) {
    is AppError.Network ->
        if (isOffline) UiText.Resource(R.string.common_error_network)
        else UiText.Resource(R.string.common_error_server_unavailable)
    is AppError.Api ->
        message?.takeIf { it.isNotBlank() }?.let { UiText.Dynamic(it) }
            ?: UiText.Resource(R.string.common_error_unknown)
    AppError.SessionExpired -> UiText.Resource(R.string.common_error_unauthorized)
}
