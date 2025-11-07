package cc.emo.emoblogbackend.data.dao

import cc.emo.emoblogbackend.data.`do`.PostLikeDo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostLikeRepository : JpaRepository<PostLikeDo, Long> {
    fun existsByPostIdAndUserId(postId: Long, userId: Long): Boolean
    fun deleteByPostIdAndUserId(postId: Long, userId: Long): Long
}
