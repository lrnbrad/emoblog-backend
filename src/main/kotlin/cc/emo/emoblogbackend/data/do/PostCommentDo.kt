package cc.emo.emoblogbackend.data.`do`

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "POST_COMMENT",
    // 為了加速查詢某篇貼文的所有留言:
    indexes = [Index(name = "idx_comment_post_created", columnList = "post_id, created_at")]
)
class PostCommentDo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: PostDo,


    // 從 Long 改用 UserDo 方便前端取得作者資訊
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false, updatable = false)
    val author: UserDo,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "like_count", nullable = false)
    var likeCount: Long = 0,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    )
