package cc.emo.emoblogbackend.adapter

import cc.emo.emoblogbackend.data.`do`.UserDo
import cc.emo.emoblogbackend.data.dto.UserProfileDto
import cc.emo.emoblogbackend.data.dto.UserProfileUpdateRequestDto

// TODO: find same birth can make use of it
fun UserDo.toUserProfileResponse() = UserProfileDto(
//    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    birth = birth,
)

fun UserDo.toUserProfileUpdate() = UserProfileUpdateRequestDto(
    firstName = firstName,
    lastName = lastName,
    birth = birth,
    newPasswordHash = passwordHash
)
