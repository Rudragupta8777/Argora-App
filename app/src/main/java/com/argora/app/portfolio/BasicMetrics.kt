package com.argora.app.portfolio

data class BasicMetrics(
    val totalValue: Double,
    val totalCost: Double,
    val totalGainLoss: Double,
    val totalGainLossPercent: Double,
    val diversificationScore: Double
)