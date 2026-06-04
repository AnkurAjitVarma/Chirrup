package me.ankur_varma.chirrup.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

data class RegisterRequest(
    @field:Email(message = "Invalid email address.")
    val email: String,
    @field:Length(min = 6, max = 16, message = "Username must be between 6 and 16 characters.")
    val username: String,
    @field:Pattern(
        regexp = "^(?=.*[\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(.{8,})$",
        message = "Password must be at least 8 characters and contain at least one digit or special character"
    )
    val password: String,
)
