package me.ankur_varma.chirrup.service.token

import me.ankur_varma.chirrup.domain.model.TokenPair
import me.ankur_varma.chirrup.domain.model.UserId
import me.ankur_varma.chirrup.infra.database.entity.RefreshTokenEntity
import me.ankur_varma.chirrup.infra.database.repository.RefreshTokenRepository
import me.ankur_varma.chirrup.infra.token.generator.TokenGenerator
import me.ankur_varma.chirrup.infra.token.validator.TokenValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.*

@Service
class TokenServiceImpl(
    @param:Value("\${jwt.access-duration}") private val accessDuration: Long,
    @param:Value("\${jwt.refresh-duration}") private val refreshDuration: Long,
    private val tokenGenerator: TokenGenerator,
    private val tokenValidator: TokenValidator,
    private val refreshTokenRepository: RefreshTokenRepository
) : TokenService {

    override fun generateTokenPairFor(id: UserId): TokenPair {
        val (access, _) = tokenGenerator.generateAccessToken(id, accessDuration * 1000)
        val (refresh, expiresAt) = tokenGenerator.generateRefreshToken(id, refreshDuration * 1000)
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = id,
                hashedToken = hashToken(refresh),
                expiresAt = expiresAt.toInstant(),
            )
        )
        return TokenPair(access, refresh)
    }

    override fun refresh(token: String): TokenPair {
        TODO("Not yet implemented")
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }
}