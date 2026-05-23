package com.mefy.platemate.presentation.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavChromeRulesTest {

    @Test
    fun destinationClass_toTopLevelDestination_mapsRootTabs() {
        assertEquals(TopLevelDestination.Search, SearchDestination::class.toTopLevelDestinationOrNull())
        assertEquals(TopLevelDestination.Discover, DiscoverDestination::class.toTopLevelDestinationOrNull())
        assertEquals(TopLevelDestination.Messages, MessagesDestination::class.toTopLevelDestinationOrNull())
        assertEquals(TopLevelDestination.Profile, ProfileDestination::class.toTopLevelDestinationOrNull())
    }

    @Test
    fun shouldShowBottomBar_isTrueOnlyForTopLevelRoutes() {
        assertTrue(SearchDestination::class.shouldShowBottomBarForRouteClass())
        assertTrue(DiscoverDestination::class.shouldShowBottomBarForRouteClass())
        assertTrue(MessagesDestination::class.shouldShowBottomBarForRouteClass())
        assertTrue(ProfileDestination::class.shouldShowBottomBarForRouteClass())

        assertFalse(SearchDetailDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(DiscoverDetailDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(ChatDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(EditProfileDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(WelcomeDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(LoginDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(RegisterDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(SessionGateDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(AuthGraphDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(MainGraphDestination::class.shouldShowBottomBarForRouteClass())
        assertFalse(null.shouldShowBottomBarForRouteClass())
    }
}
