package me.ankur_varma.chirrup.infra.database.mappers

import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.infra.database.entity.UserEntity

fun UserEntity.toUser(): User =
    User(
        id = this.id!!,
        username = this.username,
        email = this.email,
        verified = this.verified,
    )
