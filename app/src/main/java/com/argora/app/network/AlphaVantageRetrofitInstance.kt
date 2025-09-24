package com.argora.app.network

import com.argora.app.data.AlphaVantageSearchResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

object AlphaVantageRetrofitInstance {
    private const val BASE_URL = "https://www.alphavantage.co/"
    // IMPORTANT: Paste your Alpha Vantage API Key here
    private const val API_KEY = "4SRLY3CD2EOYZ2U4"

    val api: AlphaVantageApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(AlphaVantageApiService::class.java)
    }

    fun createSearchQuery(keywords: String): Map<String, String> {
        return mapOf(
            "function" to "SYMBOL_SEARCH",
            "keywords" to keywords,
            "apikey" to API_KEY
        )
    }
}

// Interface for the search endpoint
interface AlphaVantageApiService {
    @GET("query")
    suspend fun searchSymbol(@QueryMap options: Map<String, String>): AlphaVantageSearchResponse
}