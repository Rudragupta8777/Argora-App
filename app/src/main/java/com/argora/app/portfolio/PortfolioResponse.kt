package com.argora.app.portfolio

data class PortfolioResponse(
    val success: Boolean,
    val portfolio: Portfolio?,
    val analysis: PortfolioAnalysis?
)