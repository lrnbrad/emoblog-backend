package cc.emo.emoblogbackend.adapter

import cc.emo.emoblogbackend.data.`do`.UserDo
import cc.emo.emoblogbackend.data.dto.UserProfile

fun UserDo.toUserProfileDto(): UserProfile {
    return UserProfile(
        username = username,
        firstName = firstName,
        lastName = lastName,
        birthday = birthday,
    )
}
