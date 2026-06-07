package me.ankur_varma.chirrup.api.dto

data class AuthenticatedUserDto(
    val user: UserDto,
    val access_token: String,
    val refresh_token: String,
)
