package com.argora.app.models

import com.argora.app.portfolio.Recommendation

data class SimulatedState(
    val healthScore: Int,
    val projectedValue: Double,
    val recommendations: List<Recommendation>
)
