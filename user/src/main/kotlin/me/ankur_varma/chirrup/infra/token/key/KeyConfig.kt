package me.ankur_varma.chirrup.infra.token.key

import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.crypto.SecretKey


@Configuration
class KeyConfig(@param:Value("\${jwt.secret}") private val secret64: String) {
    @Bean("tokenSigningKey")
    fun tokenSigningKey(): SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret64))
}