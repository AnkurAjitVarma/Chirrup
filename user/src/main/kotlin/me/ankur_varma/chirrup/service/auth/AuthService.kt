package me.ankur_varma.chirrup.service.auth

import me.ankur_varma.chirrup.domain.exception.UserAlreadyExists
import me.ankur_varma.chirrup.domain.model.User
import me.ankur_varma.chirrup.infra.database.entity.UserEntity
import me.ankur_varma.chirrup.infra.database.mappers.toUser
import me.ankur_varma.chirrup.infra.database.repository.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
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
            throw UserAlreadyExists(email, username)
        }
    }

}