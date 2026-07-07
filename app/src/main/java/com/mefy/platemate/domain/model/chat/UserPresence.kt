package com.mefy.platemate.domain.model.chat

/**
 * Online presence of another user. [online] already reflects the reciprocal
 * visibility rule resolved by the backend (false when either side hides it).
 * [userId] is null when the room has no resolvable other participant.
 */
data class UserPresence(
    val userId: Long?,
    val online: Boolean
)
