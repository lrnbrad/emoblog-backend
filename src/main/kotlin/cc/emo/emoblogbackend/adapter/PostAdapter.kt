package cc.emo.emoblogbackend.adapter

import cc.emo.emoblogbackend.data.`do`.PostCommentDo
import cc.emo.emoblogbackend.data.`do`.PostDo
import cc.emo.emoblogbackend.data.dto.PostCommentResponse
import cc.emo.emoblogbackend.data.dto.PostResponse

fun PostDo.toPostResponse(commentCount: Long = comments.size.toLong()) = PostResponse(
    id = id,
    authorId = author.id,
    authorUsername = author.username,
    content = content,
    likeCount = likeCount,
    createdAt = createdAt ?: error("Post entity has no createdAt; ensure it is persisted"),
    updatedAt = updatedAt,
    commentCount = commentCount
)

fun PostCommentDo.toPostCommentResponse(fallbackPostId: Long? = null) = PostCommentResponse(
    id = id,
    postId = post?.id ?: fallbackPostId ?: error("Detached comment missing post reference"),
    authorId = author.id,
    content = content,
    likeCount = likeCount,
    createdAt = createdAt ?: error("Comment entity has no createdAt; ensure it is persisted"),
    updatedAt = updatedAt
)
