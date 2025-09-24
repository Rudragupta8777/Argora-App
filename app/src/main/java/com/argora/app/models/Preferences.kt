package com.argora.app.models

data class Preferences(
    val riskProfile: String,
    val alertThreshold: Int,
    val preferredIndustries: List<String>,
    val excludedIndustries: List<String>
)