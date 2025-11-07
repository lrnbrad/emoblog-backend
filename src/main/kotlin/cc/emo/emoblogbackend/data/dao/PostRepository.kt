package cc.emo.emoblogbackend.data.dao

import cc.emo.emoblogbackend.data.`do`.PostDo
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository : JpaRepository<PostDo, Long> {
    fun findAllByAuthorId(authorId: Long, pageable: Pageable): Page<PostDo>
    fun findByIdAndAuthorId(id: Long, authorId: Long): Optional<PostDo>
    fun existsByIdAndAuthorId(id: Long, authorId: Long): Boolean

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PostDo p where p.id = :id")
    fun findByIdForUpdate(@Param("id") id: Long): Optional<PostDo>
}
