package cc.emo.emoblogbackend.controller

import cc.emo.emoblogbackend.adapter.toUserProfileDto
import cc.emo.emoblogbackend.data.dto.UserProfile
import cc.emo.emoblogbackend.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.parameters.RequestBody as OpenApiRequestBody

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PutMapping("/{userId}/profile")
    @Operation(summary = "Update a user's profile")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Profile updated",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UserProfile::class),
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    )
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
