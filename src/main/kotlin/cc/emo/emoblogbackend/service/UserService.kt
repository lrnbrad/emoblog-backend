package cc.emo.emoblogbackend.service

import cc.emo.emoblogbackend.data.adapter.toUserProfileDto
import cc.emo.emoblogbackend.data.dao.UserRepository
import cc.emo.emoblogbackend.data.`do`.UserDo
import cc.emo.emoblogbackend.data.dto.AuthResponse
import cc.emo.emoblogbackend.data.dto.UserLoginRequest
import cc.emo.emoblogbackend.data.dto.UserProfile
import cc.emo.emoblogbackend.data.dto.UserRegisterRequest
import cc.emo.emoblogbackend.security.JwtService
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Service
class UserService(
    private val users: UserRepository,
    private val encoder: PasswordEncoder,
    private val jwt: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    @Transactional
    fun register(req: UserRegisterRequest): AuthResponse {

        if (users.existsUserDoByUsername(req.username)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Username already exists")
        }
        val entity = UserDo(
            username = req.username,
            passwordHash = encoder.encode(req.password)!!,
            firstName = req.firstName,
            lastName = req.lastName,
            birthday = req.birthday
        )
        users.save(entity)
        val token = jwt.generate(entity.username, mapOf("ROLE" to "USER_ROLE"))
        return AuthResponse(token, Date.from(Instant.now().plusSeconds(60 * 60)).time)
    }

    fun login(req: UserLoginRequest): AuthResponse {
        val authenticatedUsername = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(req.username, req.password)
            ).name
        } catch (ex: AuthenticationException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password", ex)
        }

        val token = jwt.generate(authenticatedUsername, mapOf("ROLE" to "USER_ROLE"))
        return AuthResponse(token, Date.from(Instant.now().plusSeconds(60 * 60)).time)
    }

    @Transactional
    fun updateProfile(userId: Long, newProfile: UserProfile): UserProfile =
        users.findById(userId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }
            .run {
                this.firstName = newProfile.firstName
                this.lastName = newProfile.lastName
                this.birthday = newProfile.birthday
                users.save(this).toUserProfileDto()
            }

    fun findUserByBirthdayParts(year: Int?, month: Int?, day: Int?): List<UserDo> {
        val match = when {
            year != null && month != null && day != null ->
                users.findByBirthday(LocalDate.of(year, month, day))

            year != null && month != null -> {
                val start = LocalDate.of(year, month, 1)
                val end = start.withDayOfMonth(start.lengthOfMonth())
                users.findAllByBirthdayBetween(start, end)
            }

            year != null -> {
                val start = LocalDate.of(year, 1, 1)
                val end = start.withDayOfYear(start.lengthOfYear())
                users.findAllByBirthdayBetween(start, end)
            }

            else -> emptyList()
        }

        return match
    }

    fun findUserByName(name: String): List<UserProfile> =
        users.findByName(name)
            .map { it.toUserProfileDto() }
}
