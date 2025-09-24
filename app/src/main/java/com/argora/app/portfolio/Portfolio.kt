package com.argora.app.portfolio

data class Portfolio(
    val holdings: List<Holding>,
    val totalValue: Double,
    val lastUpdated: String
)