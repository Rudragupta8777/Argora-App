package com.argora.app.models

data class BotMessageRequest(
    val message: String,
    val conversationId: String? = null
)