package me.ankur_varma.chirrup.infra.database.repository

import me.ankur_varma.chirrup.domain.model.UserId
import me.ankur_varma.chirrup.infra.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, UserId> {
    fun findByUsername(username: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
}