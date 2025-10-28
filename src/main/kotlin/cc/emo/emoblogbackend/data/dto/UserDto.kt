package cc.emo.emoblogbackend.data.dto

import java.time.LocalDate

// TODO: add validation method and annotations
class UserRegisterRequestDto(
    val username: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val birthday: LocalDate,
)

class UserLoginRequestDto(
    val username: String,
    val password: String,
)

class AuthResponseDto(
    val token: String,
    val expiresIn: Long,
)

data class UserProfileUpdateRequestDto(
    val firstName: String?,
    val lastName: String?,
    val birth: LocalDate?,
    val newPasswordHash: String?,
)

data class UserProfileDto(
//    val id: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val birth: LocalDate,
)
