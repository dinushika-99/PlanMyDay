package com.example.planmyday.model

import java.util.Date
import java.util.UUID

data class MoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val emoji: String,
    val note: String = "",
    val date: Date = Date()
)
