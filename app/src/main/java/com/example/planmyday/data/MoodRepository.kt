package com.example.planmyday.data

import android.content.Context
import com.example.planmyday.model.MoodEntry
import java.util.Calendar
import java.util.Date

class MoodRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("mood_preferences", Context.MODE_PRIVATE)
    private val KEY_MOOD_ENTRIES = "mood_entries"

    fun saveMoodEntry(entry: MoodEntry) {
        val entries = getMoodEntries().toMutableList()
        entries.add(entry)
        saveAllEntries(entries)
    }

    fun getMoodEntries(): List<MoodEntry> {
        val entriesString = prefs.getString(KEY_MOOD_ENTRIES, "") ?: ""
        if (entriesString.isEmpty()) return emptyList()

        return entriesString.split(";").mapNotNull { part ->
            val parts = part.split("|")
            // Format: id|emoji|note|dateInMillis
            if (parts.size == 4) {
                MoodEntry(
                    id = parts[0],
                    emoji = parts[1],
                    note = parts[2],
                    date = java.util.Date(parts[3].toLong())
                )
            } else {
                null // Skip corrupted entries
            }
        }
    }

    private fun isDateInLastWeek(dateToCheck: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val oneWeekAgo = calendar.time
        return !dateToCheck.before(oneWeekAgo)
    }

    private fun saveAllEntries(entries: List<MoodEntry>) {
        // Convert list to string format: id|emoji|note|dateInMillis;id|emoji|note|dateInMillis
        val entriesString = entries.joinToString(";") { entry ->
            "${entry.id}|${entry.emoji}|${entry.note}|${entry.date.time}"
        }
        prefs.edit().putString(KEY_MOOD_ENTRIES, entriesString).apply()
    }

    //  Clear corrupted data
    fun clearCorruptedData() {
        prefs.edit().remove(KEY_MOOD_ENTRIES).apply()
    }

    fun getLastWeekMoods(): List<MoodEntry> {
        return getMoodEntries().filter { isDateInLastWeek(it.date) }
    }
}