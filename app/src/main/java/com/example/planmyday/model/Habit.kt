package com.example.planmyday.model

import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(), // Use UUID for guaranteed uniqueness
    var name: String,
    var isCompleted: Boolean = false
)
