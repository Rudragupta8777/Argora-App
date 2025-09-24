package com.argora.app.models

import java.util.prefs.Preferences

data class User(
    val id: String,
    val email: String,
    val name: String,
    val preferences: Preferences,
    val goals: List<Goal>,
    val financialHealthScore: Int = 0
)