package me.ankur_varma.chirrup.infra.database.repository

import me.ankur_varma.chirrup.domain.model.UserId
import me.ankur_varma.chirrup.infra.database.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {

    @EntityGraph(attributePaths = ["user"])
    fun findByUserIdAndHashedToken(userId: UserId, hashedToken: String): RefreshTokenEntity?
    
}