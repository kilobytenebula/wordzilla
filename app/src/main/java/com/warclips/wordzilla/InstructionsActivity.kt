package com.warclips.wordzilla

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InstructionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_instructions)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val helpBackButton = findViewById<Button>(R.id.help_back_btn)

        helpBackButton.setOnClickListener {
            launchMainMenuActivity() // Explicit Intent for clear navigation
        }
    }

    private fun launchMainMenuActivity() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}