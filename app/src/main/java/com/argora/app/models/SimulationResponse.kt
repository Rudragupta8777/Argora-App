package com.argora.app.models

data class SimulationResponse(
    val success: Boolean,
    val scenario: String,
    val result: SimulationResult
)