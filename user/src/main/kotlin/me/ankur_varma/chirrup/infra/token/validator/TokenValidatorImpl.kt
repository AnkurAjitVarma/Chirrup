package me.ankur_varma.chirrup.infra.token.validator

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import me.ankur_varma.chirrup.domain.exception.InvalidTokenException
import me.ankur_varma.chirrup.domain.model.UserId
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class TokenValidatorImpl(@Qualifier("tokenSigningKey") val tokenSigningKey: SecretKey) : TokenValidator {

    override fun validateAccessToken(token: String): UserId? {
        val claims = parseAllClaims(token) ?: throw InvalidTokenException(token)
        if (claims["type"] == "access") {
            return UUID.fromString(claims.subject)
        }
        return null
    }

    override fun validateRefreshToken(token: String): UserId? {
        val claims = parseAllClaims(token) ?: throw InvalidTokenException(token)
        if (claims["type"] == "refresh") {
            return UUID.fromString(claims.subject)
        }
        return null
    }

    private fun parseAllClaims(token: String): Claims? = runCatching {
        Jwts.parser()
            .verifyWith(tokenSigningKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }.getOrNull()
}
