package me.ankur_varma.chirrup.domain.exception

data class UserNotVerified(val email: String) : RuntimeException("The user with email $email is not verified.")
