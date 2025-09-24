package com.argora.app.data

data class LoginRequest(
    val idToken: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val user: User?
)

data class User(
    val id: String,
    val email: String,
    val name: String
)