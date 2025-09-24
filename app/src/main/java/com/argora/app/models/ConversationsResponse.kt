package com.argora.app.models

data class ConversationsResponse(
    val success: Boolean,
    val conversations: List<Conversation>
)