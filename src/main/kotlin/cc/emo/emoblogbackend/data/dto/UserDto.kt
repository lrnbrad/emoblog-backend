package cc.emo.emoblogbackend.data.dto

import java.time.LocalDate

// TODO: add validation method and annotations
class UserRegisterRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val birthday: LocalDate,
)

class UserLoginRequest(
    val username: String,
    val password: String,
)

class AuthResponseDto(
    val token: String,
    val expiresIn: Long,
)

data class UserProfileUpdateRequest(
    val firstName: String?,
    val lastName: String?,
    val birth: LocalDate?,
    val newPasswordHash: String?,
)

data class UserProfile(
//    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
)
