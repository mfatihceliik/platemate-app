package com.mefy.platemate.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Release/profileable build için Baseline Profile üretir.
 *
 * Çalıştırma (connected device/emülatör gerekir):
 *   ./gradlew :app:generateReleaseBaselineProfile
 * Üretilen profil app modülüne (src/release/generated/baselineProfiles/) yazılır ve
 * ProfileInstaller ile release APK'ya gömülür → açılış + ilk composition AOT derlenir.
 *
 * Şu an açılış (startup) hot-path'lerini yakalar. Auth'lu ana sekme (search/discover/
 * messages/profile/settings) yollarını da kapsamak istersen, test bir oturum açıp
 * alttaki barda sekmeleri gezecek şekilde genişletilebilir (içerik açıklamaları =
 * sekme etiketleri).
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(
        packageName = PACKAGE_NAME,
        includeInStartupProfile = true,
    ) {
        pressHome()
        startActivityAndWait()
        // İlk ekranın yerleşmesini bekle (idle + içerik).
        device.waitForIdle()
        device.wait(Until.hasObject(androidx.test.uiautomator.By.pkg(PACKAGE_NAME).depth(0)), 5_000)
    }

    private companion object {
        const val PACKAGE_NAME = "com.mefy.platemate"
    }
}
