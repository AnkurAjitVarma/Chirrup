package me.ankur_varma.chirrup.service.token.auth

import me.ankur_varma.chirrup.domain.model.RefreshToken
import me.ankur_varma.chirrup.domain.model.TokenPair
import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.domain.model.ValidatedRefreshToken

interface AuthTokenService {
    fun generateTokenPairFor(user: User): TokenPair
    fun validateRefreshToken(token: String): ValidatedRefreshToken
    fun refresh(user: User, token: RefreshToken): TokenPair
    fun remove(token: RefreshToken)
}