package com.example.planmyday

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.planmyday.ui.habit.HabitFragment
import com.example.planmyday.ui.home.HomeFragment
import com.example.planmyday.ui.mood.MoodFragment
import com.example.planmyday.ui.setting.SettingFragment

class MainActivity : AppCompatActivity() {
    private val habitFragment = HabitFragment()
    private val moodFragment = MoodFragment()
    private val settingFragment = SettingFragment()
    private val homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Initialize ImageViews and TextViews
        val imgHabit : ImageView = findViewById(R.id.imgHabit)
        val txtHabit : TextView = findViewById(R.id.txtHabit)
        val imgMood : ImageView = findViewById(R.id.imgMood)
        val txtMood : TextView = findViewById(R.id.txtMood)
        val imgSetting : ImageView = findViewById(R.id.imgSetting)
        val txtSetting : TextView = findViewById(R.id.txtSetting)
        val imgHome: ImageView = findViewById(R.id.imgHome)
        val txtHome: TextView = findViewById(R.id.txtReport)

        // Habit page click
        imgHabit.setOnClickListener { loadHabit() }
        txtHabit.setOnClickListener { loadHabit() }

        // Mood page click
        imgMood.setOnClickListener { loadMood() }
        txtMood.setOnClickListener { loadMood() }

        // Setting page click
        imgSetting.setOnClickListener { loadSetting() }
        txtSetting.setOnClickListener { loadSetting() }

        imgHome.setOnClickListener { loadHome() }
        txtHome.setOnClickListener { loadHome() }

        if (savedInstanceState == null) {
            loadHabit()
        }
    }


    // Load Habit Fragment
    private fun loadHabit() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)  // Check if fragment is already loaded

        //add or replace fragment
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, habitFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, habitFragment)
                .commit()
        }
    }

    // Load Mood Fragment
    private fun loadMood() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        //add or replace fragment
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, moodFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, moodFragment)
                .commit()
        }
    }

    // Load Setting Fragment
    private fun loadSetting() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        //add or replace fragment
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, settingFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, settingFragment)
                .commit()
        }
    }

    // Load Home Fragment
    private fun loadHome() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        // Add or replace fragment
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, homeFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, homeFragment)
                .commit()
        }
    }

}





























