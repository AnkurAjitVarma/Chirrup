package me.ankur_varma.chirrup.infra.token.generator

import me.ankur_varma.chirrup.domain.model.UserId
import me.ankur_varma.chirrup.infra.token.model.Token

interface TokenGenerator {
    fun generateAccessToken(id: UserId, duration: Long): Token
    fun generateRefreshToken(id: UserId, duration: Long): Token
}