package com.mefy.platemate.data.remote.language

import com.mefy.platemate.domain.model.language.AppLanguage
import java.util.Locale
import javax.inject.Inject

class DeviceLanguageProvider @Inject constructor() {

    fun getSupportedDeviceLanguage(): AppLanguage {
        val language = Locale.getDefault().language
        return if (language.equals(AppLanguage.TR.headerValue, ignoreCase = true)) {
            AppLanguage.TR
        } else {
            AppLanguage.EN
        }
    }
}

