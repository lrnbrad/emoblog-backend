package cc.emo.emoblogbackend.adapter

import cc.emo.emoblogbackend.data.`do`.UserDo
import cc.emo.emoblogbackend.data.dto.UserProfileDto

fun UserDo.toUserProfileDto(): UserProfileDto {
    return UserProfileDto(
        username = username,
        firstName = firstName,
        lastName = lastName,
        birth = birthday,
    )
}
