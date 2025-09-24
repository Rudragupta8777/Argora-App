package com.argora.app.models

data class AuthResponse(
    val success: Boolean,
    val token: String,
    val user: User,
    val message: String
)