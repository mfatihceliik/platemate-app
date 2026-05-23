package com.mefy.platemate.data.remote.language

import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.repository.LanguagePreferenceRepository
import javax.inject.Inject

class UserPreferredLanguageProvider @Inject constructor(
    private val languagePreferenceRepository: LanguagePreferenceRepository,
    private val deviceLanguageProvider: DeviceLanguageProvider
) : LanguageProvider {

    override fun getAcceptLanguage(): String {
        val preferred = languagePreferenceRepository.peekLanguageOrNull()
        val effective = preferred ?: deviceLanguageProvider.getSupportedDeviceLanguage()
        return when (effective) {
            AppLanguage.TR -> AppLanguage.TR.headerValue
            AppLanguage.EN -> AppLanguage.EN.headerValue
        }
    }
}

