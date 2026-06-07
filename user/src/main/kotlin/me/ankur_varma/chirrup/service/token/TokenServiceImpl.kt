package me.ankur_varma.chirrup.service.token

import jakarta.transaction.Transactional
import me.ankur_varma.chirrup.domain.exception.InvalidTokenException
import me.ankur_varma.chirrup.domain.model.RefreshToken
import me.ankur_varma.chirrup.domain.model.TokenPair
import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.domain.model.ValidatedRefreshToken
import me.ankur_varma.chirrup.infra.database.entity.RefreshTokenEntity
import me.ankur_varma.chirrup.infra.database.mappers.toUser
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

    override fun generateTokenPairFor(user: User): TokenPair {
        val (access, _) = tokenGenerator.generateAccessToken(user.id, accessDuration * 1000)
        val (refresh, expiresAt) = tokenGenerator.generateRefreshToken(user.id, refreshDuration * 1000)
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = user.id,
                hashedToken = hashToken(refresh),
                expiresAt = expiresAt.toInstant(),
            )
        )
        return TokenPair(access, refresh)
    }

    override fun validateRefreshToken(token: String): ValidatedRefreshToken {
        val userId = tokenValidator.validateRefreshToken(token) ?: throw InvalidTokenException(token)
        val hash = hashToken(token)
        val refreshToken =
            refreshTokenRepository.findByUserIdAndHashedToken(userId, hash) ?: throw InvalidTokenException(
                token
            )
        val userEntity = refreshToken.user!!
        return ValidatedRefreshToken(
            user = userEntity.toUser(),
            token = RefreshToken(
                id = refreshToken.id,
                value = token,
                expiresAt = refreshToken.expiresAt
            )
        )
    }

    @Transactional
    override fun refresh(user: User, token: RefreshToken): TokenPair {
        val (access, _) = tokenGenerator.generateAccessToken(user.id, accessDuration * 1000)
        val (refresh, expiresAt) = tokenGenerator.generateRefreshToken(user.id, refreshDuration * 1000)
        refreshTokenRepository.deleteById(token.id)
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = user.id,
                hashedToken = hashToken(refresh),
                expiresAt = expiresAt.toInstant(),
            )
        )
        return TokenPair(access, refresh)
    }

    @Transactional
    override fun remove(token: RefreshToken) {
        refreshTokenRepository.deleteById(token.id)
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }
}