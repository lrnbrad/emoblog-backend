package cc.emo.emoblogbackend.data.dao

import cc.emo.emoblogbackend.data.`do`.PostCommentLikeDo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostCommentLikeRepository : JpaRepository<PostCommentLikeDo, Long> {
    fun existsByCommentIdAndUserId(commentId: Long, userId: Long): Boolean
    fun deleteByCommentIdAndUserId(commentId: Long, userId: Long): Long
}
