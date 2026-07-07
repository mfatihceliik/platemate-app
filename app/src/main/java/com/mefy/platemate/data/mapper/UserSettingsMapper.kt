package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.user.UserSettingsDto
import com.mefy.platemate.domain.model.settings.UserSettings
import javax.inject.Inject

class UserSettingsMapper @Inject constructor() : Mapper<UserSettingsDto, UserSettings> {
    override fun map(input: UserSettingsDto): UserSettings = UserSettings(
        messagingEnabled = input.messagingEnabled,
        onlineVisibilityEnabled = input.onlineVisibilityEnabled,
        messageNotificationsEnabled = input.messageNotificationsEnabled,
        friendNotificationsEnabled = input.friendNotificationsEnabled,
        plateReviewNotificationsEnabled = input.plateReviewNotificationsEnabled,
        newFollowerNotificationsEnabled = input.newFollowerNotificationsEnabled,
        reviewReplyNotificationsEnabled = input.reviewReplyNotificationsEnabled
    )
}

