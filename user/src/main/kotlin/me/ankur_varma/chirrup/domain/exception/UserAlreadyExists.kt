package me.ankur_varma.chirrup.domain.exception

data class UserAlreadyExists(val email: String) :
    RuntimeException("User with email: $email already exists.")
