package me.ankur_varma.chirrup.domain.exception

data class UserDoesNotExist(val email: String) : RuntimeException("No user with email $email exists.")
