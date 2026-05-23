package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.model.auth.PasswordStrengthLevel
import com.mefy.platemate.domain.model.auth.PasswordStrengthResult
import javax.inject.Inject

class CalculateRegisterPasswordStrengthUseCase @Inject constructor() {

    val minPasswordLength: Int
        get() = MIN_PASSWORD_LENGTH

    operator fun invoke(password: String): PasswordStrengthResult {
        val meetsMinLength = password.length >= MIN_PASSWORD_LENGTH
        if (password.isBlank()) {
            return PasswordStrengthResult(
                minLength = MIN_PASSWORD_LENGTH,
                meetsMinLength = meetsMinLength
            )
        }

        var score = 0
        if (meetsMinLength) score += 1
        if (password.length >= 10) score += 1
        if (password.any { it.isUpperCase() }) score += 1
        if (password.any { it.isLowerCase() }) score += 1
        if (password.any { it.isDigit() }) score += 1
        if (password.any { !it.isLetterOrDigit() }) score += 1

        val boundedScore = score.coerceIn(0, MAX_SCORE)
        val level = when {
            boundedScore <= 2 -> PasswordStrengthLevel.WEAK
            boundedScore <= 4 -> PasswordStrengthLevel.MEDIUM
            else -> PasswordStrengthLevel.STRONG
        }

        return PasswordStrengthResult(
            level = level,
            progress = boundedScore.toFloat() / MAX_SCORE.toFloat(),
            minLength = MIN_PASSWORD_LENGTH,
            meetsMinLength = meetsMinLength
        )
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
        const val MAX_SCORE = 6
    }
}
