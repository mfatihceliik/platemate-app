package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.user.UserSearchResultDto
import com.mefy.platemate.domain.model.user.UserSearchResult
import javax.inject.Inject

class UserSearchResultMapper @Inject constructor() : Mapper<UserSearchResultDto, UserSearchResult> {
    override fun map(input: UserSearchResultDto): UserSearchResult = UserSearchResult(
        id = input.id,
        username = input.username,
        displayName = input.displayName,
        bio = input.bio,
        profilePhotoUrl = input.profilePhotoUrl
    )
}
