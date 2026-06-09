package me.ankur_varma.chirrup.infra.database.repository

import me.ankur_varma.chirrup.infra.database.entity.VerificationTokenEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationTokenRepository : JpaRepository<VerificationTokenEntity, Long> {

    //    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = ["user"])
    fun findByHash(hash: String): VerificationTokenEntity?
}