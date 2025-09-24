package com.argora.app.portfolio

data class Recommendation(
    val type: String,
    val priority: String,
    val action: String,
    val reason: String
)