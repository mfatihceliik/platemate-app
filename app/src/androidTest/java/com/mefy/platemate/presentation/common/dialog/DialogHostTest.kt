package com.mefy.platemate.presentation.common.dialog

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DialogHostTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun noActiveDialog_rendersNothing() {
        val state = DialogHostState()

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                DialogHost(state = state)
            }
        }

        composeRule.onAllNodesWithText("Dialog Title").assertCountEquals(0)
    }

    @Test
    fun showDialog_rendersTitleMessageAndConfirm() {
        val state = DialogHostState()
        val dialog = dialogModel()

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                DialogHost(state = state)
            }
        }

        composeRule.runOnIdle {
            state.showDialog(dialog)
        }

        composeRule.onNodeWithText("Dialog Title").assertIsDisplayed()
        composeRule.onNodeWithText("Dialog Message").assertIsDisplayed()
        composeRule.onNodeWithText("OK").assertIsDisplayed()
    }

    @Test
    fun confirmClick_dismissesDialog() {
        val state = DialogHostState()
        val dialog = dialogModel()

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                DialogHost(state = state)
            }
        }

        composeRule.runOnIdle {
            state.showDialog(dialog)
        }
        composeRule.onNodeWithText("OK").performClick()

        composeRule.onAllNodesWithText("Dialog Title").assertCountEquals(0)
    }

    @Test
    fun dismissButtonClick_dismissesDialog() {
        val state = DialogHostState()
        val dialog = dialogModel(dismissText = UiText.Dynamic("Cancel"))

        composeRule.setContent {
            PlateMateTheme(darkTheme = false, dynamicColor = false) {
                DialogHost(state = state)
            }
        }

        composeRule.runOnIdle {
            state.showDialog(dialog)
        }
        composeRule.onNodeWithText("Cancel").performClick()

        composeRule.onAllNodesWithText("Dialog Title").assertCountEquals(0)
    }

    private fun dialogModel(
        dismissText: UiText? = null
    ): DialogModel {
        return DialogModel(
            title = UiText.Dynamic("Dialog Title"),
            message = UiText.Dynamic("Dialog Message"),
            confirmText = UiText.Dynamic("OK"),
            dismissText = dismissText
        )
    }
}
