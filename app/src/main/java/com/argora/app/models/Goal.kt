package com.argora.app.models

data class Goal(
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val deadline: String,
    val priority: String
)