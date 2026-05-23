package com.mefy.platemate.domain.model.auth

data class PasswordStrengthResult(
    val level: PasswordStrengthLevel = PasswordStrengthLevel.NONE,
    val progress: Float = 0f,
    val minLength: Int,
    val meetsMinLength: Boolean
)
