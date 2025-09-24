package com.argora.app.portfolio

data class PortfolioAnalysis(
    val basicMetrics: BasicMetrics,
    val aiAnalysis: AiAnalysis,
    val healthScore: Int,
    val timestamp: String
)