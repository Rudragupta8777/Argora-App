package com.argora.app.data

// A simple singleton object to hold the session token in memory.
// For a production app, you should save this to encrypted SharedPreferences.
object SessionManager {
    var apiToken: String? = null
}