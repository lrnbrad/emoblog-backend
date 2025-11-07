package cc.emo.emoblogbackend.data.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class UserRegisterRequest(
    @field:Schema(example = "user")
    val username: String,

    @field:Schema(example = "Jane")
    val firstName: String,

    @field:Schema(example = "Doe")
    val lastName: String,

    @field:Schema(example = "StrongP@ss1")
    val password: String,

    @field:Schema(example = "2000-01-01")
    var birthday: LocalDate,
)

data class UserLoginRequest(
    @field:Schema(example = "user")
    val username: String,
    @field:Schema(example = "StrongP@ss1")
    val password: String,
)

data class AuthResponse(
    @field:Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
    val token: String,

    @field:Schema(example = "1761896481521")
    val expiresIn: Long,
)

data class UserProfileUpdateRequest(
    @field:Schema(example = "Jane")
    val firstName: String?,
    @field:Schema(example = "Doe")
    val lastName: String?,
    @field:Schema(example = "2000-01-01")
    val birthday: LocalDate?,
    @field:Schema(example = "NewStrongerP@ss2")
    val newPasswordHash: String?,
)

data class UserProfile(
//    val id: Long,
    @field:Schema(example = "user")
    val username: String,
    @field:Schema(example = "Jane")
    val firstName: String,
    @field:Schema(example = "Doe")
    val lastName: String,
    @field:Schema(example = "2000-01-01")
    val birthday: LocalDate,
)
