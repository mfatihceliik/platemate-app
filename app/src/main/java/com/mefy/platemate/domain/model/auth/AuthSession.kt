package com.mefy.platemate.domain.model.auth

data class AuthSession(
    val userId: Long,
    val username: String,
    val token: String,
    val refreshToken: String? = null,
    val role: UserRole = UserRole.NORMAL,
    val premiumActive: Boolean = false
) {
    /**
     * Premium'un tek dogru sinyali: backend premium'u role=PREMIUM olarak set eder ama bazi
     * hesaplarda role NORMAL kalip premium yalnizca [premiumActive] ile tasinabiliyor. ADMIN de
     * premium sayilir (test/erisim kolayligi).
     */
    val isPremium: Boolean
        get() = premiumActive || role == UserRole.PREMIUM || role == UserRole.ADMIN
}
