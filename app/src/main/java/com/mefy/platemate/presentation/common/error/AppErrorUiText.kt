package com.mefy.platemate.presentation.common.error

import com.mefy.platemate.R
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.presentation.common.text.UiText

/**
 * [AppError] -> kullanıcıya gösterilecek metin. Tek eşleme noktası.
 *
 * `Server` backend mesajını olduğu gibi gösterir (LanguageInterceptor sayesinde
 * yerelleştirilmiş gelir); boşsa genel mesaja düşer.
 */
fun AppError.toUiText(): UiText = when (this) {
    is AppError.Network -> UiText.Resource(R.string.common_error_network)
    is AppError.Unreachable -> UiText.Resource(R.string.common_error_server_unavailable)
    AppError.SessionExpired -> UiText.Resource(R.string.common_error_unauthorized)
    is AppError.Server ->
        message?.takeIf { it.isNotBlank() }?.let { UiText.Dynamic(it) }
            ?: UiText.Resource(R.string.common_error_unknown)
}
