package com.warclips.wordzilla

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var switchSound: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<Button>(R.id.settings_back_btn)

        backButton.setOnClickListener {
            launchMainMenuActivity() // Explicit Intent for clear navigation
        }

        sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)

        switchSound = findViewById(R.id.sound_switch)
        switchSound.isChecked = sharedPreferences.getBoolean("sound_enabled", true)

        switchSound.setOnCheckedChangeListener { buttonView, isChecked ->
            // Update sound setting preference based on switch state
            sharedPreferences.edit().putBoolean("sound_enabled", isChecked).apply()
        }
    }

    private fun launchMainMenuActivity() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}