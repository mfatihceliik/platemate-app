package com.mefy.platemate.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.R
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @param:StringRes val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: KClass<out AppDestination>,
    val graphRoute: KClass<out AppDestination>,
    val graphDestination: AppDestination
) {
    Search(
        labelRes = R.string.main_tab_search,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        route = SearchDestination::class,
        graphRoute = SearchGraphDestination::class,
        graphDestination = SearchGraphDestination
    ),
    Discover(
        labelRes = R.string.main_tab_discover,
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        route = DiscoverDestination::class,
        graphRoute = DiscoverGraphDestination::class,
        graphDestination = DiscoverGraphDestination
    ),
    Messages(
        labelRes = R.string.main_tab_messages,
        selectedIcon = Icons.Filled.ChatBubble,
        unselectedIcon = Icons.Outlined.ChatBubble,
        route = MessagesDestination::class,
        graphRoute = MessagesGraphDestination::class,
        graphDestination = MessagesGraphDestination
    ),
    Profile(
        labelRes = R.string.main_tab_profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        route = ProfileDestination::class,
        graphRoute = ProfileGraphDestination::class,
        graphDestination = ProfileGraphDestination
    ),
    Settings(
        labelRes = R.string.main_tab_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = ProfileSettingsHomeDestination::class,
        graphRoute = SettingsGraphDestination::class,
        graphDestination = SettingsGraphDestination
    )
}
