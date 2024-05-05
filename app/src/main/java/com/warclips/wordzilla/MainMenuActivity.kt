package com.warclips.wordzilla

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playButton = findViewById<Button>(R.id.play_btn)
        val quitButton = findViewById<Button>(R.id.quit_btn)
        val settingsButton = findViewById<Button>(R.id.settings_btn)
        val helpButton = findViewById<Button>(R.id.help_btn)

        playButton.setOnClickListener {
            launchGameActivity() // Explicit Intent for clear navigation
        }

        helpButton.setOnClickListener {
            launchInstructionsActivity() // Explicit Intent for clear navigation
        }

        settingsButton.setOnClickListener {
            launchSettingsActivity() // Explicit Intent for clear navigation
        }

        quitButton.setOnClickListener { showExitConfirmationDialog() }
    }

    private fun launchSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun launchGameActivity() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    fun launchInstructionsActivity() {
        val intent = Intent(this, InstructionsActivity::class.java)
        startActivity(intent)
    }



    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit?")
        builder.setPositiveButton("Yes") { dialog, which ->
            finishAffinity() // Closes all activities and exit the game
        }
        builder.setNegativeButton("No", null) // Dismiss dialog on "No"
        builder.create().show()
    }

}