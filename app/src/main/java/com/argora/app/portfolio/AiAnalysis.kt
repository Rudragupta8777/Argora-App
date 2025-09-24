package com.argora.app.portfolio

data class AiAnalysis(
    val riskScore: Int,
    val liquidityScore: Int,
    val recommendations: List<Recommendation>
)