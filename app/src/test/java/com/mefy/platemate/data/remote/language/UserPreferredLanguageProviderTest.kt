package com.mefy.platemate.data.remote.language

import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.repository.LanguagePreferenceRepository
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

class UserPreferredLanguageProviderTest {

    @Test
    fun getAcceptLanguage_returnsTr_whenUserPreferenceIsTr() {
        val provider = UserPreferredLanguageProvider(
            languagePreferenceRepository = FakeLanguagePreferenceRepository(AppLanguage.TR),
            deviceLanguageProvider = DeviceLanguageProvider()
        )

        assertEquals("tr", provider.getAcceptLanguage())
    }

    @Test
    fun getAcceptLanguage_returnsEn_whenUserPreferenceIsEn() {
        val provider = UserPreferredLanguageProvider(
            languagePreferenceRepository = FakeLanguagePreferenceRepository(AppLanguage.EN),
            deviceLanguageProvider = DeviceLanguageProvider()
        )

        assertEquals("en", provider.getAcceptLanguage())
    }

    @Test
    fun getAcceptLanguage_fallsBackToDeviceLanguage_whenPreferenceMissing() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"))
            val provider = UserPreferredLanguageProvider(
                languagePreferenceRepository = FakeLanguagePreferenceRepository(null),
                deviceLanguageProvider = DeviceLanguageProvider()
            )

            assertEquals("tr", provider.getAcceptLanguage())
        } finally {
            Locale.setDefault(previous)
        }
    }

    @Test
    fun getAcceptLanguage_fallsBackToEnglish_whenDeviceLanguageNotSupported() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("de-DE"))
            val provider = UserPreferredLanguageProvider(
                languagePreferenceRepository = FakeLanguagePreferenceRepository(null),
                deviceLanguageProvider = DeviceLanguageProvider()
            )

            assertEquals("en", provider.getAcceptLanguage())
        } finally {
            Locale.setDefault(previous)
        }
    }

    private class FakeLanguagePreferenceRepository(
        initialLanguage: AppLanguage?
    ) : LanguagePreferenceRepository {

        private val state = MutableStateFlow(initialLanguage)

        override fun observeLanguage(): Flow<AppLanguage?> = state

        override suspend fun setLanguage(language: AppLanguage) {
            state.value = language
        }

        override suspend fun getLanguageOrNull(): AppLanguage? = state.value
        override fun peekLanguageOrNull(): AppLanguage? = state.value
    }
}
