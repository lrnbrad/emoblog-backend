package cc.emo.emoblogbackend.data.`do`

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "POST",
    // 為了加速查詢某個作者的所有留言: where author_id = ? order by created_at desc
    indexes = [Index(name = "idx_post_author_created", columnList = "author_id, created_at")]
)
class PostDo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    // 從 Long 改用 UserDo 方便前端取得作者資訊
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false, updatable = false)
    val author: UserDo,

    val authorUsername: String = author.username,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @OneToMany(
        mappedBy = "post",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val comments: MutableList<PostCommentDo> = mutableListOf(),

    @Column(name = "like_count", nullable = false)
    var likeCount: Long = 0,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
)
