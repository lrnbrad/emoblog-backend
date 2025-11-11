package cc.emo.emoblogbackend.data.dao

import cc.emo.emoblogbackend.data.`do`.UserDo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface UserRepository : JpaRepository<UserDo, Long> {
    @Query(
        """
        SELECT u FROM UserDo u
        WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
        """
    )
    fun findByName(@Param("name") name: String): List<UserDo>
    fun findByUsername(username: String): UserDo?
    fun findByBirthday(birthday: LocalDate): List<UserDo>
    fun findAllByBirthdayBetween(start: LocalDate, end: LocalDate): List<UserDo>
    fun deleteUserById(userId: Long): Boolean
    fun existsUserDoByUsername(username: String): Boolean
}
