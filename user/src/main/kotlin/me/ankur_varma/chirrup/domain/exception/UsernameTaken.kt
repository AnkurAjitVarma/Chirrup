package me.ankur_varma.chirrup.domain.exception

data class UsernameTaken(val username: String) : RuntimeException("Username $username is taken.")
