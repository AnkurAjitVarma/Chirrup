package me.ankur_varma.chirrup.domain.exception

data class InvalidAuthToken(val token: String) : RuntimeException("Invalid auth token: $token")
