package cc.emo.emoblogbackend.controller

import cc.emo.emoblogbackend.data.dto.AuthResponse
import cc.emo.emoblogbackend.data.dto.UserLoginRequest
import cc.emo.emoblogbackend.data.dto.UserRegisterRequest
import cc.emo.emoblogbackend.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.parameters.RequestBody as OpenApiRequestBody

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Register user",
        description = "Creates a new account and returns a JWT for subsequent requests."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "User registered",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = AuthResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "409",
            description = "Username already exists"
        )
    )
    @OpenApiRequestBody(
        required = true,
        description = "New user details"
    )
    fun register(@RequestBody @Valid request: UserRegisterRequest): AuthResponse =
        userService.register(request)


    @PostMapping("/login")
    @Operation(summary = "Authenticate user and obtain JWT")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = AuthResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Invalid credentials"
        )
    )
    @OpenApiRequestBody(
        required = true,
        description = "Credentials",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = UserLoginRequest::class),
        )]
    )
    fun login(@RequestBody @Valid request: UserLoginRequest): AuthResponse =
        userService.login(request)
}
