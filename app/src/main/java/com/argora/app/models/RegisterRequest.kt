package com.argora.app.models

data class RegisterRequest(
    val email: String,
    val name: String,
    val riskProfile: String = "balanced"
)
