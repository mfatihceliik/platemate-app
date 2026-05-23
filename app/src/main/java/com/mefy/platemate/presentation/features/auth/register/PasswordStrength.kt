package com.mefy.platemate.presentation.features.auth.register

import com.mefy.platemate.domain.model.auth.PasswordStrengthLevel

data class PasswordStrength(
    val level: PasswordStrengthLevel = PasswordStrengthLevel.NONE,
    val progress: Float = 0f
)




