package cc.emo.emoblogbackend.data.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class PostCommentCreateRequest(
    @field:Schema(example = "Nice post! Thanks for sharing.")
    val content: String
)

data class PostCommentUpdateRequest(
    @field:Schema(example = "Updating my comment after re-reading.")
    val content: String
)

data class PostCommentResponse(
    @field:Schema(example = "777")
    val id: Long,

    @field:Schema(example = "123")
    val postId: Long,

    @field:Schema(example = "user")
    val authorUsername: String,

    @field:Schema(example = "Nice post! Thanks for sharing.")
    val content: String,

    @field:Schema(example = "5")
    val likeCount: Long,

    @field:Schema(example = "2025-01-15T11:22:33")
    val createdAt: LocalDateTime,

    @field:Schema(example = "2025-01-16T08:10:00")
    val updatedAt: LocalDateTime?
)
