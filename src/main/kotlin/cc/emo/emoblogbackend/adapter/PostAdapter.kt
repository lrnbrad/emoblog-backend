package cc.emo.emoblogbackend.adapter

import cc.emo.emoblogbackend.data.`do`.PostCommentDo
import cc.emo.emoblogbackend.data.`do`.PostDo
import cc.emo.emoblogbackend.data.`do`.UserDo
import cc.emo.emoblogbackend.data.dto.PostCommentResponse
import cc.emo.emoblogbackend.data.dto.PostResponse
import cc.emo.emoblogbackend.data.dto.UserProfile
import cc.emo.emoblogbackend.data.dto.UserProfileUpdateRequest

// TODO: find same birth can make use of it
fun UserDo.toUserProfileResponse() = UserProfile(
//    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    birthday = birthday,
)

fun UserDo.toUserProfileUpdate() = UserProfileUpdateRequest(
    firstName = firstName,
    lastName = lastName,
    birthday = birthday,
    newPasswordHash = passwordHash
)

fun PostDo.toPostResponse(commentCount: Long = comments.size.toLong()) = PostResponse(
    id = id,
    authorId = author.id,
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
