package me.ankur_varma.chirrup.domain.exception

/*

 */

data class InvalidVerificationToken(val token: String) :
    RuntimeException("Invalid verification token: $token")
