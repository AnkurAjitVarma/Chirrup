package me.ankur_varma.chirrup.domain.model

import java.time.Instant

data class VerificationToken(val id: Long, val value: String, val expiresAt: Instant)
