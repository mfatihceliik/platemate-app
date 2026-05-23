package com.mefy.platemate.presentation.features.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.R
import com.mefy.platemate.presentation.navigation.TopLevelDestination
import com.mefy.platemate.presentation.theme.PlateMateTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainBottomBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun searchTab_isSelectedInitially_andCanNavigateToDiscover() {
        val searchLabel = getString(R.string.main_tab_search)
        val discoverLabel = getString(R.string.main_tab_discover)

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                var selectedTab by mutableStateOf(TopLevelDestination.Search)
                MainBottomBar(
                    selectedDestination = selectedTab,
                    onDestinationSelected = { selectedTab = it }
                )
            }
        }

        composeRule.onNodeWithContentDescription(searchLabel).assertIsSelected()
        composeRule.onNodeWithText(discoverLabel).assertIsDisplayed().performClick()
        composeRule.onNodeWithContentDescription(discoverLabel).assertIsSelected()
    }

    private fun getString(resId: Int): String =
        androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
            .getString(resId)
}
