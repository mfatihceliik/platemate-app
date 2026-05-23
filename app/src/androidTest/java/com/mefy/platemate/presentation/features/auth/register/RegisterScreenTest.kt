package com.mefy.platemate.presentation.features.auth.register

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.auth.PasswordStrengthLevel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val appContext = ApplicationProvider.getApplicationContext<android.content.Context>()

    @Test
    fun registerScreen_displaysCoreSections() {
        val heroTitle = appContext.getString(R.string.auth_register_hero_title)
        val emailLabel = appContext.getString(R.string.auth_register_email_label)
        val usernameLabel = appContext.getString(R.string.auth_register_username_label)
        val passwordLabel = appContext.getString(R.string.auth_register_password_label)
        val strengthLabel = appContext.getString(R.string.auth_register_password_strength_weak)
        val infoText = appContext.getString(R.string.auth_register_terms_info)
        val submitText = appContext.getString(R.string.auth_register_submit)
        val signInText = appContext.getString(R.string.auth_register_sign_in_link)

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                RegisterScreen(
                    state = RegisterScreenUiState(
                        username = "fatih",
                        email = "fatih@test.com",
                        password = "123456",
                        passwordStrength = PasswordStrength(
                            level = PasswordStrengthLevel.WEAK,
                            progress = 0.34f
                        ),
                        isSubmitEnabled = true
                    ),
                    onAction = {},
                    onNavigateToLoginClick = {},
                    onBackClick = {}
                )
            }
        }

        composeRule.onNodeWithText(heroTitle).assertIsDisplayed()
        composeRule.onNodeWithText(emailLabel).assertIsDisplayed()
        composeRule.onNodeWithText(usernameLabel).assertIsDisplayed()
        composeRule.onNodeWithText(passwordLabel).assertIsDisplayed()
        composeRule.onNodeWithText(strengthLabel).assertIsDisplayed()
        composeRule.onNodeWithText(infoText).assertIsDisplayed()
        composeRule.onNodeWithText(submitText).assertIsDisplayed()
        composeRule.onNodeWithText(signInText).assertIsDisplayed()
    }

    @Test
    fun submitButton_changesEnabledStateWithUiState() {
        val submitText = appContext.getString(R.string.auth_register_submit)

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                RegisterScreen(
                    state = RegisterScreenUiState(
                        email = "fatih@test.com",
                        username = "fatih",
                        password = "123456",
                        isSubmitEnabled = false
                    ),
                    onAction = {},
                    onNavigateToLoginClick = {},
                    onBackClick = {}
                )
            }
        }

        composeRule.onNodeWithText(submitText).assertIsNotEnabled()

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                RegisterScreen(
                    state = RegisterScreenUiState(
                        email = "fatih@test.com",
                        username = "fatih",
                        password = "123456",
                        isSubmitEnabled = true
                    ),
                    onAction = {},
                    onNavigateToLoginClick = {},
                    onBackClick = {}
                )
            }
        }

        composeRule.onNodeWithText(submitText).assertIsEnabled()
    }

    @Test
    fun signInLink_triggersNavigationCallback() {
        val signInText = appContext.getString(R.string.auth_register_sign_in_link)
        var clicked = false

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                RegisterScreen(
                    state = RegisterScreenUiState(),
                    onAction = {},
                    onNavigateToLoginClick = { clicked = true },
                    onBackClick = {}
                )
            }
        }

        composeRule.onNodeWithText(signInText).performClick()
        composeRule.runOnIdle {
            assertTrue(clicked)
        }
    }
}
