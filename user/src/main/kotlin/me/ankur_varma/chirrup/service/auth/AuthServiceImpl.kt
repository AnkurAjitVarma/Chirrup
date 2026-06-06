package me.ankur_varma.chirrup.service.auth

import me.ankur_varma.chirrup.domain.exception.IncorrectPassword
import me.ankur_varma.chirrup.domain.exception.UserAlreadyExists
import me.ankur_varma.chirrup.domain.exception.UserDoesNotExist
import me.ankur_varma.chirrup.domain.exception.UsernameTaken
import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.infra.database.entity.UserEntity
import me.ankur_varma.chirrup.infra.database.mappers.toUser
import me.ankur_varma.chirrup.infra.database.repository.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
) : UserService {

    override fun registerUser(email: String, username: String, password: String): User {
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

    override fun authenticateUser(email: String, password: String): User {
        val userEntity = userRepository.getByEmail(email) ?: throw UserDoesNotExist(email)
        if (!encoder.matches(password, userEntity.passwordHash)) {
            throw IncorrectPassword(email, password)
        }
        // TODO: Check if user has a verified email.
        return userEntity.toUser()
    }

}