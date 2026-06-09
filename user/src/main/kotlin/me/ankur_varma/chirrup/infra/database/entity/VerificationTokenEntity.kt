package me.ankur_varma.chirrup.infra.database.entity

import jakarta.persistence.*
import me.ankur_varma.chirrup.domain.model.UserId
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    name = "verification_tokens",
    schema = "user_service",
    indexes = [
        Index(name = "idx_verification_tokens_hash", columnList = "hash")
    ]
)
class VerificationTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(nullable = false, unique = true)
    var hash: String,
    @Column(nullable = false)
    var expiresAt: Instant,
    @Column(nullable = false)
    var userId: UserId,
    @CreationTimestamp
    var createdAt: Instant = Instant.now(),
    @Column
    var usedAt: Instant? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var user: UserEntity? = null,
)