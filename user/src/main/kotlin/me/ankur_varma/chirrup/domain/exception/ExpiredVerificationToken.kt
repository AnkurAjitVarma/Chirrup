package me.ankur_varma.chirrup.domain.exception

import me.ankur_varma.chirrup.domain.model.UserId

data class ExpiredVerificationToken(val userId: UserId, val tokenId: Long) :
    RuntimeException("Expired verification token.")
