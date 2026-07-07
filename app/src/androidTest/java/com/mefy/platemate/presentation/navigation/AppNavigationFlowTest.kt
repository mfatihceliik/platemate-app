package com.mefy.platemate.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.bottombar.MainBottomBar
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationFlowTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private val appContext = ApplicationProvider.getApplicationContext<android.content.Context>()

    @Test
    fun authGraph_startsWithOnboarding() {
        composeRule.setContent {
            navController = rememberTestNavController()
            TestNavHost(navController = navController, startDestination = AuthGraphDestination)
        }

        composeRule.runOnIdle {
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<OnboardingDestination>() == true)
        }
    }

    @Test
    fun onboarding_createAccount_navigatesToRegister() {
        val createAccountText = appContext.getString(R.string.welcome_create_account)

        composeRule.setContent {
            navController = rememberTestNavController()
            TestNavHost(navController = navController, startDestination = AuthGraphDestination)
        }

        composeRule.onNodeWithText(createAccountText).assertIsDisplayed().performClick()

        composeRule.runOnIdle {
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<RegisterDestination>() == true)
        }
    }

    @Test
    fun onboarding_signIn_navigatesToLogin() {
        val signInText = appContext.getString(R.string.welcome_sign_in)

        composeRule.setContent {
            navController = rememberTestNavController()
            TestNavHost(navController = navController, startDestination = AuthGraphDestination)
        }

        composeRule.onNodeWithText(signInText).assertIsDisplayed().performClick()

        composeRule.runOnIdle {
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<LoginDestination>() == true)
        }
    }

    @Test
    fun sessionGate_to_main_removesSessionGateFromBackStack() {
        composeRule.setContent {
            navController = rememberTestNavController()
            TestNavHost(navController = navController, startDestination = SessionGateDestination)
        }

        composeRule.runOnIdle {
            navController.navigateToMainGraphFromGate()
        }
        composeRule.runOnIdle {
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<SearchDestination>() == true)
            assertFalse(navController.popBackStack())
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<SearchDestination>() == true)
        }
    }

    @Test
    fun mainTabs_navigateBetweenGraphDestinations() {
        composeRule.setContent {
            navController = rememberTestNavController()
            TestNavHost(navController = navController, startDestination = MainGraphDestination)
        }

        composeRule.runOnIdle {
            navController.navigateToTopLevelDestination(TopLevelDestination.Discover)
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<DiscoverDestination>() == true)

            navController.navigateToTopLevelDestination(TopLevelDestination.Messages)
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<MessagesDestination>() == true)

            navController.navigateToTopLevelDestination(TopLevelDestination.Profile)
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<ProfileDestination>() == true)

            navController.navigateToTopLevelDestination(TopLevelDestination.Search)
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<SearchDestination>() == true)
        }
    }

    @Test
    fun discoverTrendNavigation_reachesDiscoverDetailRoute() {
        composeRule.setContent {
            navController = rememberTestNavController()
            TestNavHost(navController = navController, startDestination = MainGraphDestination)
        }

        composeRule.runOnIdle {
            navController.navigateToTopLevelDestination(TopLevelDestination.Discover)
            navController.navigate(DiscoverDetailDestination(id = "trend_34_abc_123"))
            assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<DiscoverDetailDestination>() == true)
        }
    }

    @Test
    fun bottomBar_isHiddenOnAuth_andVisibleOnMainTopLevelDestinations() {
        val searchTabText = appContext.getString(R.string.main_tab_search)

        composeRule.setContent {
            navController = rememberTestNavController()
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            val currentTopLevelDestination = currentDestination.toTopLevelDestinationOrNull()
            val shouldShowBottomBar = currentDestination.isTopLevelStartDestination()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    if (shouldShowBottomBar && currentTopLevelDestination != null) {
                        MainBottomBar(
                            selectedDestination = currentTopLevelDestination,
                            onDestinationSelected = navController::navigateToTopLevelDestination
                        )
                    }
                }
            ) { innerPadding ->
                TestNavHost(
                    navController = navController,
                    startDestination = AuthGraphDestination,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composeRule.onAllNodesWithText(searchTabText).assertCountEquals(0)

        composeRule.runOnIdle {
            navController.navigateToMainGraphFromGate()
        }
        composeRule.onNodeWithText(searchTabText).assertIsDisplayed()
    }

    @Test
    fun bottomBar_isHiddenOnDiscoverDetailRoute() {
        val searchTabText = appContext.getString(R.string.main_tab_search)

        composeRule.setContent {
            navController = rememberTestNavController()
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            val currentTopLevelDestination = currentDestination.toTopLevelDestinationOrNull()
            val shouldShowBottomBar = currentDestination.isTopLevelStartDestination()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    if (shouldShowBottomBar && currentTopLevelDestination != null) {
                        MainBottomBar(
                            selectedDestination = currentTopLevelDestination,
                            onDestinationSelected = navController::navigateToTopLevelDestination
                        )
                    }
                }
            ) { innerPadding ->
                TestNavHost(
                    navController = navController,
                    startDestination = MainGraphDestination,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composeRule.onNodeWithText(searchTabText).assertIsDisplayed()
        composeRule.runOnIdle {
            navController.navigateToTopLevelDestination(TopLevelDestination.Discover)
            navController.navigate(DiscoverDetailDestination(id = "trend_34_abc_123"))
        }
        composeRule.onAllNodesWithText(searchTabText).assertCountEquals(0)
    }

    @Composable
    private fun TestNavHost(
        navController: TestNavHostController,
        startDestination: AppDestination,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable<SessionGateDestination> {}
            authGraph(
                onNavigateAfterLogin = navController::navigateToMainAndClearBackStack,
                onNavigateAfterRegister = navController::navigateToMainAndClearBackStack,
                onNavigateToRegister = navController::navigateToRegister,
                onNavigateToLogin = navController::navigateToLogin,
                onShowSnackbar = {},
                onShowDialog = {},
                onBackClick = { navController.popBackStack() }
            )
            mainGraph(navController = navController)
        }
    }

    @Composable
    private fun rememberTestNavController(): TestNavHostController {
        val context = LocalContext.current
        return remember {
            TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
        }
    }
}
