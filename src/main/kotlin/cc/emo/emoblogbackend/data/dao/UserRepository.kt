package cc.emo.emoblogbackend.data.dao

import cc.emo.emoblogbackend.data.`do`.UserDo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface UserRepository : JpaRepository<UserDo, Long> {
    fun findByUsername(username: String): UserDo?
    fun findByBirthday(birthday: LocalDate): List<UserDo>
    fun findAllByBirthdayBetween(start: LocalDate, end: LocalDate): List<UserDo>
    fun deleteUserById(userId: Long): Boolean
    fun existsUserDoByUsername(username: String): Boolean
}
