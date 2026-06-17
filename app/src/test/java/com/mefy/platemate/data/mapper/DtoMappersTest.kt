package com.mefy.platemate.data.mapper

import com.mefy.platemate.data.remote.dto.friend.FriendshipDto
import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.pagination.PaginationMeta
import com.mefy.platemate.data.remote.dto.DiscoveryHomeResponseDto
import com.mefy.platemate.data.remote.dto.DiscoveryTabsDto
import com.mefy.platemate.data.remote.dto.RecentActivityDto
import com.mefy.platemate.data.remote.dto.plate.PlateDetailDto
import com.mefy.platemate.data.remote.dto.plate.PlateReviewDto
import com.mefy.platemate.data.remote.dto.report.ReportTypeDto
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.discovery.RecentActivityActionType
import com.mefy.platemate.domain.model.social.FriendshipStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class DtoMappersTest {

    @Test
    fun friendshipStatus_mapsKnownAndUnknownValues() {
        val mapper = FriendshipMapper()
        val accepted = FriendshipDto(
            id = 1,
            friendUserId = 2,
            friendUsername = "mfy",
            status = "ACCEPTED",
            createdAt = "2026-05-05T10:00:00Z"
        ).let(mapper::map)

        val unknown = FriendshipDto(
            id = 1,
            friendUserId = 2,
            friendUsername = "mfy",
            status = "SOMETHING_NEW",
            createdAt = "2026-05-05T10:00:00Z"
        ).let(mapper::map)

        assertEquals(FriendshipStatus.ACCEPTED, accepted.status)
        assertEquals(FriendshipStatus.UNKNOWN, unknown.status)
    }

    @Test
    fun reviewMapper_wrapsIsoDateAsDomainDateType() {
        val mapper = PlateReviewMapper()
        val review = PlateReviewDto(
            id = 1,
            plateCode = "34ABC123",
            rating = 5,
            comment = "Good driver",
            reviewStatusCode = "APPROVED",
            userId = 7,
            username = "mfy",
            createdAt = "2026-05-05T10:00:00Z",
            updatedAt = "2026-05-06T10:00:00Z"
        ).let(mapper::map)

        assertEquals(AppDateTime("2026-05-05T10:00:00Z"), review.createdAt)
        assertEquals(AppDateTime("2026-05-06T10:00:00Z"), review.updatedAt)
        assertEquals("34ABC123", review.plateCode)
        assertEquals(7L, review.userId)
    }

    @Test
    fun pageMapper_mapsItemsAndMetaPaginationShape() {
        val mapper = PlateReviewPageMapper(PlateReviewMapper())
        val page = PagedResult(
            items = listOf(
                PlateReviewDto(
                    id = 34,
                    plateCode = "34ABC123",
                    rating = 4,
                    comment = "Careful driver",
                    reviewStatusCode = "PENDING_REVIEW",
                    userId = 15,
                    username = "ali",
                    createdAt = "2026-05-12T09:30:00",
                    updatedAt = "2026-05-12T09:30:00"
                )
            ),
            meta = PaginationMeta(
                page = 0,
                size = 20,
                totalElements = 45,
                totalPages = 3,
                hasNext = true,
                hasPrevious = false
            )
        ).let(mapper::map)

        assertEquals(1, page.items.size)
        assertEquals("ali", page.items.first().reviewerUsername)
        assertEquals(0, page.meta.page)
        assertEquals(20, page.meta.size)
        assertEquals(45L, page.meta.totalElements)
        assertEquals(3, page.meta.totalPages)
        assertEquals(true, page.meta.hasNext)
        assertEquals(false, page.meta.hasPrevious)
    }

    @Test
    fun discoveryMapper_mapsKnownRecentActivityActionType() {
        val mapper = DiscoveryMapper()
        val result = mapper.map(
            DiscoveryHomeResponseDto(
                dailyStats = null,
                tabs = null,
                cityStats = null,
                topCityPlates = null,
                recentActivities = listOf(
                    RecentActivityDto(
                        username = "fatih",
                        plateCode = "34ABC123",
                        actionType = "REPORT_ADDED",
                        occurredAt = "2026-05-18T10:00:00",
                        rating = 0.0,
                        comment = "",
                        reportTypeCode = "",
                        reportTypeLabel = ""
                    )
                )
            )
        )

        assertEquals(RecentActivityActionType.REPORT_ADDED, result.recentActivities.first().actionType)
    }

    @Test
    fun discoveryMapper_mapsUnknownRecentActivityActionTypeToUnknown() {
        val mapper = DiscoveryMapper()
        val result = mapper.map(
            DiscoveryHomeResponseDto(
                dailyStats = null,
                tabs = null,
                cityStats = null,
                topCityPlates = null,
                recentActivities = listOf(
                    RecentActivityDto(
                        username = "fatih",
                        plateCode = "34ABC123",
                        actionType = "SOMETHING_NEW",
                        occurredAt = "2026-05-18T10:00:00",
                        rating = 0.0,
                        comment = "",
                        reportTypeCode = "",
                        reportTypeLabel = ""
                    ),
                    RecentActivityDto(
                        username = "ali",
                        plateCode = "06XYZ987",
                        actionType = null,
                        occurredAt = "2026-05-18T11:00:00",
                        rating = 0.0,
                        comment = "",
                        reportTypeCode = "",
                        reportTypeLabel = ""
                    )
                )
            )
        )

        assertEquals(RecentActivityActionType.UNKNOWN, result.recentActivities[0].actionType)
        assertEquals(RecentActivityActionType.UNKNOWN, result.recentActivities[1].actionType)
    }

    @Test
    fun discoveryMapper_mapsTabPlateCityNameAndReportTypeColor() {
        val mapper = DiscoveryMapper()
        val result = mapper.map(
            DiscoveryHomeResponseDto(
                dailyStats = null,
                tabs = DiscoveryTabsDto(
                    trendPlates = listOf(
                        PlateDetailDto(
                            plateCode = "43ABC123",
                            cityName = "Kutahya",
                            ratingAverage = 4.2,
                            reviewCount = 11,
                            todaySearchCount = 2,
                            todayReviewCount = 3,
                            todayReportCount = 1,
                            todayWeightedReportScore = 5.0,
                            score = 7.2,
                            lastActivityAt = "2026-05-18T10:00:00",
                            reportTypeDto = listOf(
                                ReportTypeDto(
                                    code = "DANGER",
                                    label = "Danger",
                                    description = "Danger behavior",
                                    iconKey = "alert",
                                    severity = "HIGH",
                                    colorHex = "#FF0000",
                                    weight = 10,
                                    sortOrder = 1
                                )
                            )
                        )
                    ),
                    dangerousPlates = emptyList(),
                    goodDriverPlates = emptyList(),
                    newPlates = emptyList()
                ),
                cityStats = null,
                topCityPlates = null,
                recentActivities = null
            )
        )

        assertEquals("Kutahya", result.tabs.trendPlates.first().cityName)
        assertEquals("#FF0000", result.tabs.trendPlates.first().topReportType.first().colorHex)
    }
}
