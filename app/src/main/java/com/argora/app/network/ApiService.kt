package com.argora.app.network

import com.argora.app.models.*
import com.argora.app.portfolio.AddHoldingRequest
import com.argora.app.portfolio.PortfolioResponse
import com.argora.app.portfolio.UploadPortfolioRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Authentication
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ProfileResponse>

    @PUT("auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ProfileResponse>

    // Portfolio
    @GET("portfolio")
    suspend fun getPortfolio(@Header("Authorization") token: String): Response<PortfolioResponse>

    @POST("portfolio/holdings")
    suspend fun addHolding(
        @Header("Authorization") token: String,
        @Body request: AddHoldingRequest
    ): Response<ApiResponse>

    @POST("portfolio/upload")
    suspend fun uploadPortfolio(
        @Header("Authorization") token: String,
        @Body request: UploadPortfolioRequest
    ): Response<ApiResponse>

    // Bot
    @POST("bot/message")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: BotMessageRequest
    ): Response<BotResponse>

    @GET("bot/conversations")
    suspend fun getConversations(@Header("Authorization") token: String): Response<ConversationsResponse>

    // Simulations
    @POST("simulation/what-if")
    suspend fun runSimulation(
        @Header("Authorization") token: String,
        @Body request: SimulationRequest
    ): Response<SimulationResponse>

    @GET("simulation/templates")
    suspend fun getSimulationTemplates(@Header("Authorization") token: String): Response<TemplatesResponse>

    // Alerts
    @GET("alerts")
    suspend fun getAlerts(@Header("Authorization") token: String): Response<AlertsResponse>

    @PATCH("alerts/read-all")
    suspend fun markAllAlertsRead(@Header("Authorization") token: String): Response<ApiResponse>
}