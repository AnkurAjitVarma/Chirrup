package me.ankur_varma.chirrup.api.controller

import jakarta.validation.Valid
import me.ankur_varma.chirrup.api.dto.*
import me.ankur_varma.chirrup.api.mappers.toUserDto
import me.ankur_varma.chirrup.service.token.TokenService
import me.ankur_varma.chirrup.service.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/auth"])
class AuthController(
    private val userService: UserService,
    private val tokenService: TokenService
) {
    @PostMapping(value = ["/register"])
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody body: RegisterRequest): UserDto {
        return userService.registerUser(body.email, body.username, body.password).toUserDto()
    }

    @PostMapping(value = ["/login"])
    fun login(@RequestBody body: LoginRequest): AuthenticatedUserDto {
        val user = userService.authenticateUser(body.email, body.password)
        val (access, refresh) = tokenService.generateTokenPairFor(user)
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
        val (user, token) = tokenService.validateRefreshToken(body.token)
        val (access, refresh) = tokenService.refresh(user, token)
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
        val (_, token) = tokenService.validateRefreshToken(body.token)
        tokenService.remove(token)
    }
}