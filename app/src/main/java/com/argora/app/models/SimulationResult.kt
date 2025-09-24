package com.argora.app.models

import com.argora.app.portfolio.PortfolioAnalysis

data class SimulationResult(
    val currentState: PortfolioAnalysis,
    val simulatedState: SimulatedState,
    val impactAnalysis: ImpactAnalysis
)