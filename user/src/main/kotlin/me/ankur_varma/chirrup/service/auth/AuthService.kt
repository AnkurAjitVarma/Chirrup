package me.ankur_varma.chirrup.service.auth

import me.ankur_varma.chirrup.domain.exception.IncorrectPassword
import me.ankur_varma.chirrup.domain.exception.UserAlreadyExists
import me.ankur_varma.chirrup.domain.exception.UserDoesNotExist
import me.ankur_varma.chirrup.domain.exception.UsernameTaken
import me.ankur_varma.chirrup.domain.model.AuthenticatedUser
import me.ankur_varma.chirrup.domain.model.Token
import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.domain.model.UserId
import me.ankur_varma.chirrup.infra.database.entity.RefreshTokenEntity
import me.ankur_varma.chirrup.infra.database.entity.UserEntity
import me.ankur_varma.chirrup.infra.database.mappers.toUser
import me.ankur_varma.chirrup.infra.database.repository.RefreshTokenRepository
import me.ankur_varma.chirrup.infra.database.repository.UserRepository
import me.ankur_varma.chirrup.service.auth.jwt.JWTGenerator
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwtGenerator: JWTGenerator,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun registerUser(email: String, username: String, password: String): User {
        try {
            return userRepository.save(
                UserEntity(
                    email = email,
                    username = username,
                    passwordHash = encoder.encode(password)!!,
                )
            ).toUser()
        } catch (e: DataIntegrityViolationException) {
            throw when {
                userRepository.existsByEmail(email) -> UserAlreadyExists(email)
                userRepository.existsByUsername(username) -> UsernameTaken(username)
                else -> e
            }
        }
    }

    fun authenticateUser(email: String, password: String): AuthenticatedUser {
        val userEntity = userRepository.getByEmail(email) ?: throw UserDoesNotExist(email)
        if (!encoder.matches(password, userEntity.passwordHash)) {
            throw IncorrectPassword(email, password)
        }
        //TODO: check if the email is verified
        val accessToken = jwtGenerator.generateAccessToken(userEntity.id!!)
        val refreshToken = jwtGenerator.generateRefreshToken(userEntity.id!!)
        saveRefreshToken(userEntity.id!!, refreshToken)

        return AuthenticatedUser(
            user = userEntity.toUser(),
            accessToken = accessToken.token,
            refreshToken = refreshToken.token
        )
    }

    private fun saveRefreshToken(userId: UserId, refreshToken: Token) {
        val hash = hashToken(refreshToken.token)
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = refreshToken.expiresAt.toInstant(),
                hashedToken = hash
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

}