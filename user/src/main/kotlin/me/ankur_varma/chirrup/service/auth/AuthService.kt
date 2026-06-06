package me.ankur_varma.chirrup.service.auth

import me.ankur_varma.chirrup.domain.model.User

interface AuthService {
    fun registerUser(email: String, username: String, password: String): User
    fun authenticateUser(email: String, password: String): User
}