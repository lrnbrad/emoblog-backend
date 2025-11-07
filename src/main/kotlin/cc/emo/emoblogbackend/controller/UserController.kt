package cc.emo.emoblogbackend.controller

import cc.emo.emoblogbackend.adapter.toUserProfileDto
import cc.emo.emoblogbackend.data.dto.UserProfile
import cc.emo.emoblogbackend.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody as OpenApiRequestBody
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PutMapping("/{userId}/profile")
    @Operation(summary = "Update a user's profile")
    @OpenApiRequestBody(
        required = true,
        description = "User profile payload",
        content = [Content(
            mediaType = "application/json",
        )]
    )
    fun updateProfile(
        @PathVariable userId: Long,
        @RequestBody @Valid request: UserProfile
    ): UserProfile = userService.updateProfile(userId, request)

    @GetMapping("/search/by-birthday")
    @Operation(summary = "Search users by birthday")
    fun findByBirthday(
        @RequestParam(required = false) year: Int?,
        @RequestParam(required = false) month: Int?,
        @RequestParam(required = false) day: Int?
    ): List<UserProfile> =
        userService.findUserByBirthdayParts(year, month, day)
            .map { it.toUserProfileDto() }
}
