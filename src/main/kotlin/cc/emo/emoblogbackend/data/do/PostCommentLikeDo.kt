package cc.emo.emoblogbackend.data.`do`

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
@Table(
    name = "POST_COMMENT_LIKE",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_comment_like_user",
            columnNames = ["comment_id", "user_id"]
        )
    ],
    indexes = [
        Index(name = "idx_comment_like_comment", columnList = "comment_id"),
        Index(name = "idx_comment_like_user", columnList = "user_id")
    ]
)
class PostCommentLikeDo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val comment: PostCommentDo,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val user: UserDo,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,
)
