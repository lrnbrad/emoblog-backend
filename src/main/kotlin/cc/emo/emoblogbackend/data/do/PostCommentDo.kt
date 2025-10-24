package cc.emo.emoblogbackend.data.`do`

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "post_comment")
class PostCommentDo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: PostDo?,

    val authorId: Long,

    var content: String,

    var likeCount: Long,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime? = null,
)
