package cc.emo.emoblogbackend.data.dao

import cc.emo.emoblogbackend.data.`do`.PostCommentDo
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface PostCommentRepository {
    fun createComment(postCommentDo: PostCommentDo): PostCommentDo
    fun findAllByPostId(postId: Long): Page<PostCommentDo>
    fun updateCommentContent(commentId: Long, userId: Long, newContent: String): PostCommentDo
    fun deleteById(commentId: Long, authorId: Long): Boolean
    fun addLikeCount(commentId: Long): PostCommentDo
    fun decreaseLikeCount(commentId: Long): PostCommentDo
}
