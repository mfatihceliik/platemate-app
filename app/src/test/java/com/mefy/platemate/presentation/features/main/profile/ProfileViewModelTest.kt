package com.mefy.platemate.presentation.features.main.profile

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.pagination.ReviewStatusTotals
import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.domain.model.auth.AuthSession
import com.mefy.platemate.domain.model.profile.ProfileFriendRequest
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.model.settings.UserSettings
import com.mefy.platemate.domain.model.social.Friendship
import com.mefy.platemate.domain.model.social.SocialPlatform
import com.mefy.platemate.domain.repository.AuthRepository
import com.mefy.platemate.domain.repository.ProfileRepository
import com.mefy.platemate.domain.repository.SocialLinkRepository
import com.mefy.platemate.domain.repository.SocialRepository
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.profile.GetProfileUseCase
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.domain.usecase.social.GetPendingFriendRequestsUseCase
import com.mefy.platemate.domain.usecase.sociallink.GetSocialPlatformsUseCase
import com.mefy.platemate.presentation.common.global.DefaultGlobalUiEventBus
import com.mefy.platemate.presentation.features.main.profile.mapper.DefaultProfileUiMapper
import com.mefy.platemate.presentation.features.main.profile.reducer.ProfileStateReducer
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialLoading_true_thenSessionLoadsContent() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val authRepository = FakeAuthRepository(initialSession = null)
        val profileRepository = FakeProfileRepository(sampleProfile())
        val viewModel = createViewModel(authRepository, profileRepository)

        assertTrue(viewModel.uiState.value.isInitialLoading)

        authRepository.emitSession(userId = 42L)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isInitialLoading)
        assertEquals("caner", state.header.username)
        assertEquals(2, state.stats.size)
        assertEquals(2, state.activities.size)
    }

    @Test
    fun action_plateReviewClicked_emitsNavigateReviewDetail() = runTest(mainDispatcherRule.dispatcher.scheduler) {
        val viewModel = createViewModel(
            FakeAuthRepository(AuthSession(42L, "u", "t")),
            FakeProfileRepository(sampleProfile())
        )
        advanceUntilIdle()

        val deferred = async { viewModel.uiEffect.first() }
        viewModel.onAction(ProfileUiAction.PlateReviewClicked("34AB1234", 1L))
        assertEquals(
            ProfileUiEffect.NavigateToReviewDetail("34AB1234", 1L),
            deferred.await()
        )
    }

    private fun createViewModel(
        authRepository: FakeAuthRepository,
        profileRepository: FakeProfileRepository
    ): ProfileViewModel = ProfileViewModel(
        observeSessionUseCase = ObserveSessionUseCase(authRepository),
        getProfileUseCase = GetProfileUseCase(profileRepository),
        getSocialPlatformsUseCase = GetSocialPlatformsUseCase(FakeSocialLinkRepository()),
        getPendingFriendRequestsUseCase = GetPendingFriendRequestsUseCase(FakeSocialRepository()),
        profileUiMapper = DefaultProfileUiMapper(
            formatTurkishPlateInputUseCase = FormatTurkishPlateInputUseCase(),
            validateTurkishPlateUseCase = ValidateTurkishPlateUseCase()
        ),
        profileStateReducer = ProfileStateReducer(),
        globalUiEventBus = DefaultGlobalUiEventBus()
    )

    private class FakeAuthRepository(initialSession: AuthSession?) : AuthRepository {
        private val sessionState = MutableStateFlow(initialSession)
        override val session: Flow<AuthSession?> = sessionState.asStateFlow()

        fun emitSession(userId: Long) {
            sessionState.value = AuthSession(userId, "user$userId", "token$userId")
        }

        override suspend fun login(email: String, password: String) = AppResult.Error(AppError.Api("unused"))
        override suspend fun register(username: String, email: String, password: String) =
            AppResult.Error(AppError.Api("unused"))
        override suspend fun refreshSession() = AppResult.Error(AppError.Api("unused"))
        override suspend fun changePassword(currentPassword: String, newPassword: String): AppResult<Unit> =
            AppResult.Error(AppError.Api("unused"))
        override suspend fun logout() {
            sessionState.value = null
        }
    }

    private class FakeSocialLinkRepository : SocialLinkRepository {
        override suspend fun addSocialLink(platform: String, url: String): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun updateSocialLink(link: SocialMediaLink): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun deleteSocialLink(id: Long): AppResult<Unit> = AppResult.Success(Unit)
        override suspend fun getSocialPlatforms(): AppResult<List<SocialPlatform>> = AppResult.Success(emptyList())
    }

    private class FakeSocialRepository : SocialRepository {
        override suspend fun sendFriendRequest(addresseeId: Long): AppResult<Unit> = AppResult.Error(AppError.Api("unused"))
        override suspend fun acceptFriendRequest(id: Long): AppResult<Unit> = AppResult.Error(AppError.Api("unused"))
        override suspend fun rejectFriendRequest(id: Long): AppResult<Unit> = AppResult.Error(AppError.Api("unused"))
        override suspend fun removeFriend(id: Long): AppResult<Unit> = AppResult.Error(AppError.Api("unused"))
        override suspend fun getFriends(): AppResult<List<Friendship>> = AppResult.Success(emptyList())
        override suspend fun getPendingRequests(): AppResult<List<Friendship>> = AppResult.Success(emptyList())
        override suspend fun getSentRequests(): AppResult<List<Friendship>> = AppResult.Success(emptyList())
    }

    private class FakeProfileRepository(
        private val profile: UserProfile
    ) : ProfileRepository {
        override suspend fun getProfile(userId: Long): AppResult<UserProfile> = AppResult.Success(profile)
        override suspend fun updateProfile(
            userId: Long,
            displayName: String?,
            username: String?,
            bio: String?,
            profilePhotoUrl: String?
        ): AppResult<Unit> = AppResult.Success(Unit)
    }

    private fun sampleProfile(): UserProfile = UserProfile(
        id = 42L,
        email = "caner@platemate.com",
        username = "caner",
        totalFriendCounts = 12,
        averageGivenRating = 4.9,
        reviewCount = 8,
        joinedAt = "2026-05-27T10:36:23.347Z",
        premiumActive = true,
        premiumUntil = "2026-06-30T00:00:00Z",
        userSettings = UserSettings(
            messagingEnabled = true,
            messageNotificationsEnabled = true,
            friendNotificationsEnabled = true
        ),
        reviewStatusCounts = ReviewStatusTotals(
            approved = 8,
            pendingReview = 2,
            rejected = 1,
            removedByUser = 0,
            removedByModerator = 0,
            removedByLegalRequest = 0
        ),
        evaluationTotals = null,
        socialMediaLinks = listOf(SocialMediaLink(id = 1L, platform = "INSTAGRAM", url = "https://x")),
        plateReviews = listOf(
            Review(
                id = 1L,
                plateCode = "34AB1234",
                rating = 5,
                comment = "danger",
                reviewStatus = "APPROVED",
                userId = 10L,
                reviewerUsername = "u",
                createdAt = null,
                updatedAt = null
            )
        ),
        friendRequests = listOf(
            ProfileFriendRequest(
                id = 2L,
                requesterUserId = 99L,
                requesterUsername = "fatih",
                addresseeUserId = 42L,
                addresseeUsername = "caner",
                statusCode = "PENDING",
                createdAt = null,
                respondedAt = null,
                lastActionAt = null
            )
        )
    )
}
