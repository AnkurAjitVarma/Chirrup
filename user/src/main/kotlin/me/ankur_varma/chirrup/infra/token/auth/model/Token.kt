package me.ankur_varma.chirrup.infra.token.auth.model

import java.util.*

data class Token(val token: String, val expiresAt: Date)
