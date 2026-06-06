package me.ankur_varma.chirrup.service.token

import me.ankur_varma.chirrup.domain.model.TokenPair
import me.ankur_varma.chirrup.domain.model.UserId

interface TokenService {
    fun generateTokenPairFor(id: UserId): TokenPair
    fun refresh(token: String): TokenPair
}