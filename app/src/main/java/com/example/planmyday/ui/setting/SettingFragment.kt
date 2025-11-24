package com.example.planmyday.ui.setting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.planmyday.R
import com.example.planmyday.data.SettingsRepository

class SettingFragment : Fragment() {

    private lateinit var settingsRepository: SettingsRepository

    // Hydration Settings Views
    private lateinit var switchHydration: SwitchCompat
    private lateinit var spinnerInterval: Spinner
    private lateinit var hydrationSettings: LinearLayout

    // Notification Settings
    private lateinit var switchNotifications: SwitchCompat

    // User Settings
    private lateinit var etUserName: EditText
    private lateinit var spinnerTheme: Spinner

    // Buttons
    private lateinit var btnSave: Button
    private lateinit var btnClearData: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        settingsRepository = SettingsRepository(requireContext())
        initializeViews(view)
        loadCurrentSettings()
        setupClickListeners()

        return view
    }

    private fun initializeViews(view: View) {
        // Hydration Settings
        switchHydration = view.findViewById(R.id.switchHydration)
        spinnerInterval = view.findViewById(R.id.spinnerInterval)
        hydrationSettings = view.findViewById(R.id.hydrationSettings)

        // Notification Settings
        switchNotifications = view.findViewById(R.id.switchNotifications)

        // User Settings
        etUserName = view.findViewById(R.id.etUserName)
        spinnerTheme = view.findViewById(R.id.spinnerTheme)

        // Buttons
        btnSave = view.findViewById(R.id.btnSave)
        btnClearData = view.findViewById(R.id.btnClearData)

        setupSpinners()
    }

    private fun setupSpinners() {
        // Hydration Interval Spinner - using string array from resources
        val intervalAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.interval_entries,
            android.R.layout.simple_spinner_item
        )
        intervalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInterval.adapter = intervalAdapter

        // Theme Spinner - using string array from resources
        val themeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.theme_entries,
            android.R.layout.simple_spinner_item
        )
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTheme.adapter = themeAdapter
    }

    private fun loadCurrentSettings() {
        // Hydration Settings
        val hydrationEnabled = settingsRepository.isHydrationReminderEnabled()
        switchHydration.isChecked = hydrationEnabled
        hydrationSettings.visibility = if (hydrationEnabled) View.VISIBLE else View.GONE

        val currentInterval = settingsRepository.getHydrationInterval()
        spinnerInterval.setSelection(currentInterval - 1) // Adjust for 0-based index

        // Notification Settings
        switchNotifications.isChecked = settingsRepository.areNotificationsEnabled()

        // User Settings
        etUserName.setText(settingsRepository.getUserName())

        val currentTheme = settingsRepository.getTheme()
        val themePosition = when (currentTheme) {
            "light" -> 0
            "dark" -> 1
            else -> 2 // system
        }
        spinnerTheme.setSelection(themePosition)
    }

    private fun setupClickListeners() {
        // Hydration Toggle
        switchHydration.setOnCheckedChangeListener { _, isChecked ->
            hydrationSettings.visibility = if (isChecked) View.VISIBLE else View.GONE

            // Add this: When user enables the reminder, ask for permission
            if (isChecked) {
                checkAndRequestNotificationPermission()
            } else {
                // User turned it off, you might want to cancel existing reminders
                cancelWaterReminders()
            }
        }

        // Save Button - NOW WORKING!
        btnSave.setOnClickListener {
            saveSettings()
            Toast.makeText(requireContext(), "Settings saved!", Toast.LENGTH_SHORT).show()
        }

        // Clear Data Button - NOW WORKING!
        btnClearData.setOnClickListener {
            showClearDataConfirmation()
        }
    }

    private fun saveSettings() {
        // Hydration Settings
        settingsRepository.setHydrationReminderEnabled(switchHydration.isChecked)
        settingsRepository.setHydrationInterval(spinnerInterval.selectedItemPosition + 1) // 1-4 hours

        // Notification Settings
        settingsRepository.setNotificationsEnabled(switchNotifications.isChecked)

        // User Settings
        settingsRepository.setUserName(etUserName.text.toString())

        val selectedTheme = when (spinnerTheme.selectedItemPosition) {
            0 -> "light"
            1 -> "dark"
            else -> "system"
        }
        settingsRepository.setTheme(selectedTheme)

        // Show confirmation toast using string resource
        Toast.makeText(requireContext(), R.string.save_settings_button, Toast.LENGTH_SHORT).show()
    }

    private fun showClearDataConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Clear All Data")
            .setMessage("This will reset all your habits, mood entries, and settings. This action cannot be undone.")
            .setPositiveButton("Clear All") { _, _ ->
                settingsRepository.clearAllData()
                Toast.makeText(requireContext(), "All data cleared! Please restart the app.", Toast.LENGTH_LONG).show()
                // Note: App needs restart to see cleared habits/moods
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, start scheduling reminders :cite[1]
            scheduleWaterReminders()
        } else {
            // Permission denied, reset the toggle switch
            switchHydration.isChecked = false
            Toast.makeText(requireContext(), "Permission is needed for reminders", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            // Check if permission is already granted
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                // Permission already exists, schedule reminders
                scheduleWaterReminders()
            } else {
                // Request the permission from the user
                requestPermissionLauncher.launch(permission)
            }
        } else {
            // For older Android versions, schedule reminders directly
            scheduleWaterReminders()
        }
    }

    private fun scheduleWaterReminders() {
        val interval = settingsRepository.getHydrationInterval()
        settingsRepository.scheduleWaterReminders(interval)
        Toast.makeText(requireContext(), "Water reminders enabled every $interval hours!", Toast.LENGTH_SHORT).show()
    }

    private fun cancelWaterReminders() {
        settingsRepository.cancelWaterReminders()
        Toast.makeText(requireContext(), "Water reminders disabled", Toast.LENGTH_SHORT).show()
    }



}