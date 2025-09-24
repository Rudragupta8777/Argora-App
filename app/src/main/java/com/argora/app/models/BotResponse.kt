package com.argora.app.models

data class BotResponse(
    val success: Boolean,
    val response: String,
    val conversationId: String,
    val metadata: BotMetadata
)