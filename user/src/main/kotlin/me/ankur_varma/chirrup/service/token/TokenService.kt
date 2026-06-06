package me.ankur_varma.chirrup.service.token

import me.ankur_varma.chirrup.domain.model.TokenPair
import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.domain.model.ValidatedRefreshToken

interface TokenService {
    fun generateTokenPairFor(user: User): TokenPair
    fun validateRefreshToken(token: String): ValidatedRefreshToken
    fun refresh(user: User, tokenId: Long): TokenPair
}