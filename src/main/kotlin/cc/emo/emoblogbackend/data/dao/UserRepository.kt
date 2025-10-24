package cc.emo.emoblogbackend.data.dao

import cc.emo.emoblogbackend.data.`do`.UserDo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface UserRepository : JpaRepository<UserDo, Long> {
    fun createUser(userDo: UserDo): UserDo
    fun findByUserId(userId: Long): UserDo?
    fun findAllByBirthday(birthday: LocalDate): List<UserDo>

    @Query("SELECT u FROM UserDo u WHERE u.birth= :y")
    fun findByBirthdayYear(@Param("y") year: Int): List<UserDo>

    @Query("SELECT u FROM UserDo u WHERE u.birth= :m")
    fun findByBirthdayMonth(@Param("m") month: Int): List<UserDo>

    @Query("SELECT u FROM UserDo u WHERE month(u.birth) = :m and day(u.birth) = :d")
    fun findByBirthdayMonthDay(@Param("m") month: Int, @Param("d") day: Int): List<UserDo>

    fun updateUserBirthById(userId: Long, newBirth: LocalDate): UserDo
    fun deleteUserById(userId: Long): Boolean

}