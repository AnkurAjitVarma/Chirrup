package me.ankur_varma.chirrup.api.dto

import me.ankur_varma.chirrup.domain.model.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val verified: Boolean,
)
