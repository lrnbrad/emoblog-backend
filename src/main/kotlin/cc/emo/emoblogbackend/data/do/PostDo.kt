package cc.emo.emoblogbackend.data.`do`

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class PostDo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column
    val authorId: Long,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @OneToMany(
        mappedBy = "post",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val comments: MutableList<PostCommentDo>,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime? = null
)
