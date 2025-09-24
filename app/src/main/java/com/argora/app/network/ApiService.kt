package com.argora.app.network

import com.argora.app.data.AddHoldingRequest
import com.argora.app.data.LoginRequest
import com.argora.app.data.LoginResponse
import com.argora.app.data.PortfolioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/portfolio")
    suspend fun getPortfolio(@Header("Authorization") token: String): Response<PortfolioResponse>

    @POST("api/portfolio/holdings")
    suspend fun addHolding(@Header("Authorization") token: String, @Body addHoldingRequest: AddHoldingRequest): Response<Unit> // Assuming a simple success/fail response
}