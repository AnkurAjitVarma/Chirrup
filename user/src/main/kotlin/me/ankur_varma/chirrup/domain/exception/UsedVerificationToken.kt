package me.ankur_varma.chirrup.domain.exception

import me.ankur_varma.chirrup.domain.model.UserId

data class UsedVerificationToken(val userId: UserId, val tokenId: Long) :
    RuntimeException("The varification token has already been used.")
