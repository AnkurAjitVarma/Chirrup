package me.ankur_varma.chirrup.infra.token.validator

import me.ankur_varma.chirrup.domain.model.UserId

interface TokenValidator {
    fun validateAccessToken(token: String): UserId?
    fun validateRefreshToken(token: String): UserId?
}