package me.ankur_varma.chirrup.infra.database.repository

import me.ankur_varma.chirrup.infra.database.entity.VerificationTokenEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface VerificationTokenRepository : JpaRepository<VerificationTokenEntity, Long> {

    //    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = ["user"])
    fun findByHash(hash: String): VerificationTokenEntity?
    fun deleteByExpiresAtLessThan(now: Instant)
}