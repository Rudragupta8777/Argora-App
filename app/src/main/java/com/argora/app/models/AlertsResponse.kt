package com.argora.app.models

data class AlertsResponse(
    val success: Boolean,
    val alerts: List<Alert>,
    val unreadCount: Int
)