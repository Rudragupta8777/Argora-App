package com.argora.app.models

data class SimulationRequest(
    val scenario: String,
    val parameters: Map<String, Any>
)