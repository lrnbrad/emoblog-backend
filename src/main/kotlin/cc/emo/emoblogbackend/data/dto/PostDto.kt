package cc.emo.emoblogbackend.data.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class PostCreateRequest(
    @field:Schema(example = "Hello, world! This is my first post.")
    val content: String
)

data class PostUpdateRequest(
    @field:Schema(example = "Edited content with some tweaks.")
    val content: String
)

data class PostResponse(
    @field:Schema(example = "123")
    val id: Long,

    @field:Schema(example = "42")
    val authorId: Long,

    @field:Schema(example = "ericQQ")
    val authorUsername: String,

    @field:Schema(example = "Hello, world! This is my first post.")
    val content: String,

    @field:Schema(example = "1")
    val likeCount: Long,

    @field:Schema(example = "2025-01-15T10:20:30")
    val createdAt: LocalDateTime,

    @field:Schema(example = "2025-01-16T09:05:00")
    val updatedAt: LocalDateTime?,

    @field:Schema(example = "3")
    val commentCount: Long
)
