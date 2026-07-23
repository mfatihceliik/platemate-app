package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.follow.FollowListItemDto
import com.mefy.platemate.domain.model.follow.FollowListItem
import javax.inject.Inject

class FollowListItemMapper @Inject constructor() : Mapper<FollowListItemDto, FollowListItem> {
    override fun map(input: FollowListItemDto): FollowListItem = FollowListItem(
        id = input.id,
        username = input.username,
        displayName = input.displayName,
        bio = input.bio,
        profilePhotoUrl = input.profilePhotoUrl,
        isFollowing = input.isFollowing ?: false
    )
}
