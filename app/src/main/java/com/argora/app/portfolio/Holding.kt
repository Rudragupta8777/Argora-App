package com.argora.app.portfolio

data class Holding(
    val assetType: String,
    val name: String,
    val ticker: String,
    val exchange: String? = null,
    val quantity: Double,
    val avgBuyPrice: Double,
    val currentPrice: Double? = null,
    val purchaseDate: String,
    val currency: String = "INR",
    val sector: String? = null
)