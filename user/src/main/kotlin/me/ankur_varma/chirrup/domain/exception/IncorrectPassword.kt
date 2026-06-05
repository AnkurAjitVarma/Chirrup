package me.ankur_varma.chirrup.domain.exception

data class IncorrectPassword(val email: String, val password: String) :
    RuntimeException("Incorrect password entered for user with email $email.")
