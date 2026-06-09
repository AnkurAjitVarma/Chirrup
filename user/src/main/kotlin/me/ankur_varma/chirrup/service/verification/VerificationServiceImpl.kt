package me.ankur_varma.chirrup.service.verification

import jakarta.transaction.Transactional
import me.ankur_varma.chirrup.domain.exception.ExpiredVerificationToken
import me.ankur_varma.chirrup.domain.exception.InvalidVerificationToken
import me.ankur_varma.chirrup.domain.exception.UsedVerificationToken
import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.domain.model.VerificationToken
import me.ankur_varma.chirrup.infra.database.entity.VerificationTokenEntity
import me.ankur_varma.chirrup.infra.database.repository.UserRepository
import me.ankur_varma.chirrup.infra.database.repository.VerificationTokenRepository
import me.ankur_varma.chirrup.infra.token.verification.generator.VerificationTokenGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class VerificationServiceImpl(
    @param:Value("\${chirrup.verification.duration}") private val duration: Long,
    private val generator: VerificationTokenGenerator,
    private val tokenRepository: VerificationTokenRepository,
    private val userRepository: UserRepository,
) : VerificationService {

    override fun generateVerificationTokenFor(user: User): VerificationToken {
        val token = generator.generateSecureToken()
        val expiresAt = Instant.now().plus(duration, ChronoUnit.HOURS)
        val tokenEntity = tokenRepository.save(
            VerificationTokenEntity(
                hash = hashToken(token),
                expiresAt = expiresAt,
                userId = user.id
            )
        )
        return VerificationToken(tokenEntity.id, token, expiresAt)
    }

    @Transactional
    override fun verify(token: String) {
        val hash = hashToken(token)
        val tokenEntity = tokenRepository.findByHash(hash) ?: throw InvalidVerificationToken(token)
        val now = Instant.now()
        if (tokenEntity.expiresAt.isBefore(now)) {
            throw ExpiredVerificationToken(tokenEntity.user!!.id!!, tokenEntity.id)
        }
        if (tokenEntity.usedAt != null) {
            throw UsedVerificationToken(tokenEntity.user!!.id!!, tokenEntity.id)
        }
        val userEntity = tokenEntity.user!!
        userRepository.save(
            userEntity.apply {
                this.verified = true
            }
        )
        tokenRepository.save(
            tokenEntity.apply {
                this.usedAt = now
            }
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

}