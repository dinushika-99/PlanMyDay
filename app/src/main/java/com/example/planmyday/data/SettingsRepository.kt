package com.example.planmyday.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.planmyday.workers.WaterReminderWorker
import java.util.concurrent.TimeUnit

class SettingsRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    // Hydration Reminder Settings
    fun setHydrationReminderEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("hydration_enabled", enabled).apply()
    }

    fun isHydrationReminderEnabled(): Boolean {
        return prefs.getBoolean("hydration_enabled", false)
    }

    fun setHydrationInterval(hours: Int) {
        prefs.edit().putInt("hydration_interval", hours).apply()
    }

    fun getHydrationInterval(): Int {
        return prefs.getInt("hydration_interval", 2) // Default 2 hours
    }

    // Notification Settings
    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean("notifications_enabled", true)
    }

    // User Preferences
    fun setUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }

    fun getUserName(): String {
        return prefs.getString("user_name", "") ?: ""
    }

    fun setTheme(theme: String) {
        prefs.edit().putString("app_theme", theme).apply()
    }

    fun getTheme(): String {
        return prefs.getString("app_theme", "light") ?: "light"
    }

    // Data Management - Clear ALL app data
    fun clearAllData() {
        // Clear settings
        prefs.edit().clear().apply()

        // Clear habits data
        val habitPrefs = context.getSharedPreferences("planmyday_habits", Context.MODE_PRIVATE)
        habitPrefs.edit().clear().apply()

        // Clear mood data
        val moodPrefs = context.getSharedPreferences("mood_preferences", Context.MODE_PRIVATE)
        moodPrefs.edit().clear().apply()
    }

    fun scheduleWaterReminders(intervalHours: Int) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            intervalHours.toLong(), TimeUnit.HOURS
            //2,TimeUnit.MINUTES                         //  for testing purposes
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "water_reminder_work",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelWaterReminders() {
        WorkManager.getInstance(context).cancelUniqueWork("water_reminder_work")
    }
}