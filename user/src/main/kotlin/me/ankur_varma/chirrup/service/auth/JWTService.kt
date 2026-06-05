package me.ankur_varma.chirrup.service.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import me.ankur_varma.chirrup.domain.exception.InvalidTokenException
import me.ankur_varma.chirrup.domain.model.UserId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import kotlin.io.encoding.Base64

@Service
class JWTService(
    @param:Value("\${jwt.access-duration}") private val accessDuration: Long,
    @param:Value("\${jwt.refresh-duration}") private val refreshDuration: Long,
    @param:Value("\${jwt.secret}") private val secret64: String
) {
    private val key = Keys.hmacShaKeyFor(Base64.decode(secret64))
    private val accessTokenDurationMs = accessDuration * 1000
    private val refreshTokenDurationMs = refreshDuration * 1000

    fun generateAccessToken(userId: UserId): String {
        return generateToken(
            userId = userId,
            type = Type.ACCESS,
            duration = accessTokenDurationMs
        )
    }

    fun generateRefreshToken(userId: UserId): String {
        return generateToken(
            userId = userId,
            type = Type.REFRESH,
            duration = refreshTokenDurationMs
        )
    }

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val type = claims["type"]
        return type == Type.ACCESS
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val type = claims["type"]
        return type == Type.REFRESH
    }

    fun getUserIdFromToken(token: String): UserId {
        val claims = parseAllClaims(token) ?: throw InvalidTokenException(token)
        return UUID.fromString(claims.subject)

    }

    private fun generateToken(
        userId: UserId,
        type: Type,
        duration: Long
    ): String {
        val now = Date()
        val expiresAt = Date(now.time + duration)
        return Jwts.builder()
            .subject(userId.toString())
            .claims(mapOf("type" to type))
            .issuedAt(now)
            .expiration(expiresAt)
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    private fun parseAllClaims(token: String): Claims? = runCatching {
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }.getOrNull()

    enum class Type(val value: String) {
        ACCESS("access"),
        REFRESH("refresh");

        override fun toString() = value
    }
}