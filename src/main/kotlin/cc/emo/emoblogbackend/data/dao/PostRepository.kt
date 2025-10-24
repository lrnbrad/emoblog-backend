package cc.emo.emoblogbackend.data.dao

import cc.emo.emoblogbackend.data.`do`.PostDo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<PostDo, Long> {
    fun createPost(post: PostDo): PostDo
    fun findAllByAuthorId(authorId: Long, pageable: Pageable): Page<PostDo>
    fun updatePost(post: PostDo): PostDo
    fun deleteByIdAndAuthorId(postId: Long, authorId: Long): Boolean
}