package com.mefy.platemate.presentation.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigateToAuthGraphFromGate() {
    navigate(AuthGraphDestination) {
        popUpTo<SplashDestination> { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToMainGraphFromGate() {
    navigate(MainGraphDestination) {
        popUpTo<SplashDestination> { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToMainAndClearBackStack() {
    navigate(MainGraphDestination) {
        popUpTo<AuthGraphDestination> { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToAuthAndClearBackStack() {
    navigate(AuthGraphDestination) {
        popUpTo(graph.findStartDestination().id) { inclusive = true }
        launchSingleTop = true
    }
}


fun NavHostController.navigateToLogin(prefillIdentifier: String? = null) {
    if (currentDestination?.route != LoginDestination(prefillIdentifier).toString()) {
        navigate(LoginDestination(prefillIdentifier = prefillIdentifier)) {
            launchSingleTop = true
        }
    }
}

fun NavHostController.navigateToRegister(prefillIdentifier: String? = null) {
    if (currentDestination?.route != RegisterDestination(prefillIdentifier).toString()) {
        navigate(RegisterDestination(prefillIdentifier = prefillIdentifier)) {
            launchSingleTop = true
        }
    }
}

fun NavHostController.navigateToDiscoverDetail(id: String) {
    navigate(DiscoverDetailDestination(id = id)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToDiscoverCityPlates(cityId: Int, cityName: String) {
    navigate(DiscoverCityPlatesDestination(cityId = cityId, cityName = cityName)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToDiscoverFilter() {
    navigate(DiscoverFilterDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToDiscoverCityFilter() {
    navigate(DiscoverCityFilterDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToDiscoverRatingFilter() {
    navigate(DiscoverRatingFilterDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToDiscoverReportTypeFilter() {
    navigate(DiscoverReportTypeFilterDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToDiscoverWindowFilter() {
    navigate(DiscoverWindowFilterDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToSearchDetail(id: String) {
    navigate(SearchDetailDestination(id = id)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToCameraScanner() {
    navigate(CameraScannerDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToReview(plateCode: String) {
    navigate(ReviewDestination(plateCode = plateCode)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToPlateActions(plateCode: String) {
    navigate(PlateActionsDestination(plateCode = plateCode)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToRemovalRequest(plateId: Long, plateCode: String) {
    navigate(PlateRemovalRequestDestination(plateId = plateId, plateCode = plateCode)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToEditReview(plateCode: String, reviewId: Long, rating: Int, comment: String) {
    navigate(
        ReviewDestination(
            plateCode = plateCode,
            reviewId = reviewId,
            initialRating = rating,
            initialComment = comment
        )
    ) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToEditProfile() {
    navigate(EditProfileDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToProfileFriends(initialTab: Int = 0) {
    navigate(ProfileFriendsDestination(initialTab = initialTab)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToProfileReviewList(status: String) {
    navigate(ProfileReviewListDestination(initialStatus = status)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToProfileNotificationPreferences() {
    navigate(ProfileNotificationPreferencesDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToProfilePremiumInfo() {
    navigate(ProfilePremiumInfoDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToProfileChangePassword() {
    navigate(ProfileChangePasswordDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToProfileThemeColor() {
    navigate(ProfileThemeColorDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToProfileCardStyle() {
    navigate(ProfileCardStyleDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToProfileLanguage() {
    navigate(ProfileLanguageDestination) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToUserProfile(userId: String) {
    navigate(UserProfileDestination(userId = userId)) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateToTopLevelDestination(destination: TopLevelDestination) {
    navigate(destination.graphDestination) {
        popUpTo(MainGraphDestination) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
