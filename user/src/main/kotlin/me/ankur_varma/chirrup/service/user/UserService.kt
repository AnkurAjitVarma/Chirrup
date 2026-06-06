package me.ankur_varma.chirrup.service.user

import me.ankur_varma.chirrup.domain.model.User

interface UserService {
    fun registerUser(email: String, username: String, password: String): User
    fun authenticateUser(email: String, password: String): User
}