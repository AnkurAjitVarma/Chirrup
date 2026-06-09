package me.ankur_varma.chirrup.api.controller

import jakarta.validation.Valid
import me.ankur_varma.chirrup.api.dto.*
import me.ankur_varma.chirrup.api.mappers.toUserDto
import me.ankur_varma.chirrup.service.token.AuthTokenService
import me.ankur_varma.chirrup.service.user.UserService
import me.ankur_varma.chirrup.service.verification.VerificationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/auth"])
class AuthController(
    private val userService: UserService,
    private val authTokenService: AuthTokenService,
    private val verificationService: VerificationService
) {
    @PostMapping(value = ["/register"])
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody body: RegisterRequest): UserDto {
        val user = userService.registerUser(body.email, body.username, body.password)
        val token = verificationService.generateVerificationTokenFor(user)
        println("-----------------------------------------------------------------------")
        println(token)
        println("-----------------------------------------------------------------------")
        return user.toUserDto()
    }

    @PostMapping(value = ["/login"])
    fun login(@RequestBody body: LoginRequest): AuthenticatedUserDto {
        val user = userService.authenticateUser(body.email, body.password)
        val (access, refresh) = authTokenService.generateTokenPairFor(user)
        return AuthenticatedUserDto(
            user = user.toUserDto(),
            access_token = access,
            refresh_token = refresh
        )
    }

    @PostMapping(value = ["/refresh"])
    fun refresh(
        @RequestBody body: RefreshRequest
    ): AuthenticatedUserDto {
        val (user, token) = authTokenService.validateRefreshToken(body.token)
        val (access, refresh) = authTokenService.refresh(user, token)
        return AuthenticatedUserDto(
            user = user.toUserDto(),
            access_token = access,
            refresh_token = refresh
        )
    }

    @PostMapping(value = ["/logout"])
    fun logout(
        @RequestBody body: LogoutRequest
    ) {
        val (_, token) = authTokenService.validateRefreshToken(body.token)
        authTokenService.remove(token)
    }

    @GetMapping(value = ["/verify"])
    fun verify(
        @RequestParam token: String
    ) {
        verificationService.verify(token)
    }
}