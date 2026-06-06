package me.ankur_varma.chirrup.infra.token.generator

import io.jsonwebtoken.Jwts
import me.ankur_varma.chirrup.domain.model.UserId
import me.ankur_varma.chirrup.infra.token.model.Token
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class TokenGeneratorImpl(@Qualifier("tokenSigningKey") val tokenSigningKey: SecretKey) : TokenGenerator {

    override fun generateAccessToken(
        id: UserId,
        duration: Long
    ): Token {
        val now = Date()
        val expiresAt = Date(now.time + duration)
        val token = Jwts.builder()
            .subject(id.toString())
            .claims(mapOf("type" to "access"))
            .issuedAt(now)
            .expiration(expiresAt)
            .signWith(tokenSigningKey, Jwts.SIG.HS256)
            .compact()
        return Token(token, expiresAt)
    }

    override fun generateRefreshToken(
        id: UserId,
        duration: Long
    ): Token {
        val now = Date()
        val expiresAt = Date(now.time + duration)
        val token = Jwts.builder()
            .subject(id.toString())
            .claims(mapOf("type" to "refresh"))
            .issuedAt(now)
            .expiration(expiresAt)
            .signWith(tokenSigningKey, Jwts.SIG.HS256)
            .compact()
        return Token(token, expiresAt)
    }
}