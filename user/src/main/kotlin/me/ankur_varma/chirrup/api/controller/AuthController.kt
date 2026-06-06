package me.ankur_varma.chirrup.api.controller

import jakarta.validation.Valid
import me.ankur_varma.chirrup.api.dto.AuthenticatedUserDto
import me.ankur_varma.chirrup.api.dto.LoginRequest
import me.ankur_varma.chirrup.api.dto.RegisterRequest
import me.ankur_varma.chirrup.api.dto.UserDto
import me.ankur_varma.chirrup.api.mappers.toUserDto
import me.ankur_varma.chirrup.service.auth.AuthService
import me.ankur_varma.chirrup.service.token.TokenService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/auth"])
class AuthController(
    private val authService: AuthService,
    private val tokenService: TokenService
) {
    @PostMapping(value = ["/register"])
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: RegisterRequest): UserDto {
        return authService.registerUser(request.email, request.username, request.password).toUserDto()
    }

    @PostMapping(value = ["/login"])
    fun login(@RequestBody credentials: LoginRequest): AuthenticatedUserDto {
        val user = authService.authenticateUser(credentials.email, credentials.password)
        val (access, refresh) = tokenService.generateTokenPairFor(user.id)
        return AuthenticatedUserDto(
            user = user.toUserDto(),
            accessToken = access,
            refreshToken = refresh
        )
    }
}