package me.ankur_varma.chirrup.infra.token.verification.generator

import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.*

@Component
class VerificationTokenGeneratorImpl : VerificationTokenGenerator {
    override fun generateSecureToken(): String {
        val bytes = ByteArray(32) { 0 }
        val secureRandom = SecureRandom()
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
    }
}