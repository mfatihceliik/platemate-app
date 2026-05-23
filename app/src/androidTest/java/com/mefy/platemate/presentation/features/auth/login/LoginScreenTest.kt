package com.mefy.platemate.presentation.features.auth.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PlateMateTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val appContext = ApplicationProvider.getApplicationContext<android.content.Context>()

    @Test
    fun loginScreen_displaysCoreSections() {
        val heroTitle = appContext.getString(R.string.auth_login_hero_title)
        val emailLabel = appContext.getString(R.string.auth_login_email_label)
        val passwordLabel = appContext.getString(R.string.auth_login_password_label)
        val submitText = appContext.getString(R.string.auth_login_submit)
        val noAccountText = appContext.getString(R.string.auth_login_no_account)
        val registerLinkText = appContext.getString(R.string.auth_login_register_link)

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                LoginScreen(
                    state = LoginScreenUiState(
                        email = "ornek@mail.com",
                        password = "123456",
                        isSubmitEnabled = true
                    ),
                    onAction = {},
                    onNavigateToRegisterClick = {},
                    onBackClick = {}
                )
            }
        }

        composeRule.onNodeWithText(heroTitle).assertIsDisplayed()
        composeRule.onNodeWithText(emailLabel).assertIsDisplayed()
        composeRule.onNodeWithText(passwordLabel).assertIsDisplayed()
        composeRule.onNodeWithText(submitText).assertIsDisplayed()
        composeRule.onNodeWithText(noAccountText).assertIsDisplayed()
        composeRule.onNodeWithText(registerLinkText).assertIsDisplayed()
    }

    @Test
    fun submitButton_changesEnabledStateWithUiState() {
        val submitText = appContext.getString(R.string.auth_login_submit)

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                LoginScreen(
                    state = LoginScreenUiState(
                        email = "ornek@mail.com",
                        password = "123456",
                        isSubmitEnabled = false
                    ),
                    onAction = {},
                    onNavigateToRegisterClick = {},
                    onBackClick = {}
                )
            }
        }

        composeRule.onNodeWithText(submitText).assertIsNotEnabled()

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                LoginScreen(
                    state = LoginScreenUiState(
                        email = "ornek@mail.com",
                        password = "123456",
                        isSubmitEnabled = true
                    ),
                    onAction = {},
                    onNavigateToRegisterClick = {},
                    onBackClick = {}
                )
            }
        }

        composeRule.onNodeWithText(submitText).assertIsEnabled()
    }

    @Test
    fun registerLink_triggersNavigationCallback() {
        val registerLinkText = appContext.getString(R.string.auth_login_register_link)
        var clicked = false

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                LoginScreen(
                    state = LoginScreenUiState(),
                    onAction = {},
                    onNavigateToRegisterClick = { clicked = true },
                    onBackClick = {}
                )
            }
        }

        composeRule.onNodeWithText(registerLinkText).performClick()
        composeRule.runOnIdle {
            assertTrue(clicked)
        }
    }

    @Test
    fun backButton_triggersBackCallback() {
        val backLabel = appContext.getString(R.string.common_back)
        var clicked = false

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                LoginScreen(
                    state = LoginScreenUiState(),
                    onAction = {},
                    onNavigateToRegisterClick = {},
                    onBackClick = { clicked = true }
                )
            }
        }

        composeRule.onNodeWithContentDescription(backLabel).performClick()
        composeRule.runOnIdle {
            assertTrue(clicked)
        }
    }
}
