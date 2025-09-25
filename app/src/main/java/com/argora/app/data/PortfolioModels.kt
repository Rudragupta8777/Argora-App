package com.argora.app.data

import com.google.gson.annotations.SerializedName

// For fetching the portfolio
data class PortfolioResponse(
    val success: Boolean,
    val portfolio: Portfolio?,
    val analysis: PortfolioAnalysis?,
    val message: String? // To handle the "Portfolio is empty" message
)

data class Portfolio(
    val holdings: List<Holding>,
    val totalValue: Double,
    val lastUpdated: String
)

data class Holding(
    @SerializedName("_id") val id: String,
    val assetType: String,
    val name: String,
    val ticker: String,
    val quantity: Double,
    val avgBuyPrice: Double,
    val currentPrice: Double,
    val purchaseDate: String
)

data class PortfolioAnalysis(
    val basicMetrics: BasicMetrics,
    val aiAnalysis: AiAnalysis?, // Make this nullable for empty portfolios
    val healthScore: Int
)

data class BasicMetrics(
    val totalValue: Double,
    val totalGainLossPercent: Double,
    val diversificationScore: Double
)

// This class was missing
data class AiAnalysis(
    val riskScore: Int,
    val liquidityScore: Int,
    val recommendations: List<Recommendation>,
    // --- ADD THIS NEW FIELD ---
    val holdings_with_real_time_prices: List<Holding>? // It's a list of your existing Holding model
)

// This class was missing
data class Recommendation(
    val type: String,
    val priority: String,
    val action: String,
    val reason: String
)

// For adding a new holding (no changes here)
data class AddHoldingRequest(
    val holding: NewHolding
)

data class NewHolding(
    val assetType: String = "stock",
    val name: String,
    val ticker: String,
    val quantity: Double,
    val avgBuyPrice: Double,
    val purchaseDate: String
)

// For Alpha Vantage search results (no changes here)
data class AlphaVantageSearchResponse(
    val bestMatches: List<StockMatch>
)

data class StockMatch(
    @SerializedName("1. symbol") val symbol: String,
    @SerializedName("2. name") val name: String,
    @SerializedName("4. region") val region: String
) {
    override fun toString(): String {
        return "$name ($symbol)"
    }
}