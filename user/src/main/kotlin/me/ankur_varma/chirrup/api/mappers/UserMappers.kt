package me.ankur_varma.chirrup.api.mappers

import me.ankur_varma.chirrup.api.dto.UserDto
import me.ankur_varma.chirrup.domain.model.User

fun User.toUserDto(): UserDto =
    UserDto(
        id = this.id,
        email = this.email,
        username = this.username,
        verified = this.verified,
    )