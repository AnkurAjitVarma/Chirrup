package me.ankur_varma.chirrup.domain.exception

data class InvalidTokenException(val token: String) : RuntimeException("Invalid token: $token")
