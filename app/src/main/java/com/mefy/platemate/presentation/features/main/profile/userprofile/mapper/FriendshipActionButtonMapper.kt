package com.mefy.platemate.presentation.features.main.profile.userprofile.mapper

import androidx.annotation.StringRes
import com.mefy.platemate.R
import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.domain.model.profile.ProfileFriendshipStatus
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import javax.inject.Inject

data class FriendshipActionButtonUiModel(
    @param:StringRes val labelRes: Int,
    val variant: PMButtonVariant,
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)

data class FriendshipActionButtonMappingInput(
    val friendshipStatus: ProfileFriendshipStatus,
    val onAddFriendClick: () -> Unit,
    val onCancelRequestClick: () -> Unit,
    val onAcceptRequestClick: () -> Unit,
    val onRemoveFriendClick: () -> Unit
)

class FriendshipActionButtonMapper @Inject constructor() :
    Mapper<FriendshipActionButtonMappingInput, FriendshipActionButtonUiModel> {

    override fun map(input: FriendshipActionButtonMappingInput): FriendshipActionButtonUiModel =
        when (input.friendshipStatus) {
            ProfileFriendshipStatus.PENDING_SENT -> FriendshipActionButtonUiModel(
                labelRes = R.string.user_profile_cancel_request,
                variant = PMButtonVariant.Outlined,
                onClick = input.onCancelRequestClick
            )
            ProfileFriendshipStatus.PENDING_RECEIVED -> FriendshipActionButtonUiModel(
                labelRes = R.string.user_profile_accept_request,
                variant = PMButtonVariant.Filled,
                onClick = input.onAcceptRequestClick
            )
            ProfileFriendshipStatus.FRIENDS -> FriendshipActionButtonUiModel(
                labelRes = R.string.user_profile_remove_friend_button,
                variant = PMButtonVariant.Outlined,
                isDestructive = true,
                onClick = input.onRemoveFriendClick
            )
            ProfileFriendshipStatus.NONE, ProfileFriendshipStatus.UNKNOWN -> FriendshipActionButtonUiModel(
                labelRes = R.string.user_profile_add_friend,
                variant = PMButtonVariant.Filled,
                onClick = input.onAddFriendClick
            )
        }
}
