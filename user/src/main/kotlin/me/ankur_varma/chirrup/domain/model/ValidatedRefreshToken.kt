package me.ankur_varma.chirrup.domain.model

data class ValidatedRefreshToken(val user: User, val token: RefreshToken)
