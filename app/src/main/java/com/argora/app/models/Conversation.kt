package com.argora.app.models

data class Conversation(
    val id: String,
    val title: String,
    val lastMessage: String,
    val updatedAt: String
)