package com.example.planmyday.data

import android.content.Context
import com.example.planmyday.model.Habit

class HabitRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("planmyday_habits", Context.MODE_PRIVATE)

    fun saveHabits(habits: List<Habit>) {
        val habitString = habits.joinToString(";") { "${it.id}|${it.name}|${it.isCompleted}" }
        prefs.edit().putString("habits", habitString).apply()
    }

    fun getHabits(): List<Habit> {
        val habitString = prefs.getString("habits", "") ?: ""
        if (habitString.isEmpty()) return emptyList()

        return habitString.split(";").mapNotNull { part ->
            val parts = part.split("|")
            when {
                parts.size == 3 -> Habit(
                    id = parts[0],
                    name = parts[1],
                    isCompleted = parts[2].toBoolean()
                )
                parts.size == 2 -> Habit(
                    name = parts[0],
                    isCompleted = parts[1].toBoolean()
                )
                else -> null
            }
        }
    }

    fun clearCorruptedData() {
        prefs.edit().remove("habits").apply()
    }
}