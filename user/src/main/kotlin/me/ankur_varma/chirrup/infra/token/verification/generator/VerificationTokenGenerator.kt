package me.ankur_varma.chirrup.infra.token.verification.generator

interface VerificationTokenGenerator {
    fun generateSecureToken(): String
}