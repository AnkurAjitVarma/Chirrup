package me.ankur_varma.chirrup.infra.database.repository

import me.ankur_varma.chirrup.domain.model.UserId
import me.ankur_varma.chirrup.infra.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, UserId> {
    fun getByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
}