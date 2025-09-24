package com.argora.app.models

import java.util.prefs.Preferences

data class UpdateProfileRequest(
    val preferences: Preferences,
    val goals: List<Goal>
)