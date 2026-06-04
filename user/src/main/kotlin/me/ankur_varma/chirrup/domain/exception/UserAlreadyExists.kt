package me.ankur_varma.chirrup.domain.exception

data class UserAlreadyExists(val email: String, val username: String) :
    RuntimeException("User with email: $email or username: $username already exists.")
