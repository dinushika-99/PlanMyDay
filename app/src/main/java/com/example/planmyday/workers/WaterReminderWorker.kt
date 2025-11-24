package com.example.planmyday.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.planmyday.data.SettingsRepository
import com.example.planmyday.utils.NotificationHelper

class WaterReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val settingsRepository = SettingsRepository(applicationContext)

        // Only show notification if notifications are enabled in settings
        if (settingsRepository.areNotificationsEnabled()) {
            NotificationHelper(applicationContext).showWaterReminder()
        }
        return Result.success()
    }
}