package me.ankur_varma.chirrup.domain.model

import java.util.UUID

typealias UserId = UUID

data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val verified: Boolean,
)
