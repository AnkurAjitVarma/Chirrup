package me.ankur_varma.chirrup.service.verification

import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.domain.model.VerificationToken

interface VerificationService {
    fun generateVerificationTokenFor(user: User): VerificationToken
    fun verify(token: String)
}