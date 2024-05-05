package com.warclips.wordzilla

import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.warclips.wordzilla.databinding.ActivityGameBinding
import androidx.lifecycle.ViewModelProvider


class GameActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var correctGuessSoundId: Int = 0
    private var highScoreSoundId: Int = 0
    private var incorrectSoundId: Int = 0
    private var isSoundEnabled: Boolean = true
    private lateinit var viewModel: GameViewModel
    private lateinit var binding : ActivityGameBinding
    private val dictionary = mutableListOf (
        "house", "train", "apple", "beach", "river", "happy", "learn", "lunch"
    )
    private var currentWord = ""
    private var bestScore = 0
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        // Observe changes to the score and update the UI
        binding.scoreDynTxt.text = viewModel.getScore().toString()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sound effect
        correctGuessSoundId = soundPool.load(this, R.raw.correct_guess_sound, 1)
        highScoreSoundId =  soundPool.load(this, R.raw.high_score_sound, 1)
        incorrectSoundId = soundPool.load(this, R.raw.incorrect_sound, 1)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("game_prefs", MODE_PRIVATE)
        bestScore = sharedPreferences?.getInt("best_score", 0) ?: 0 // Load best score on launch (null check)
        binding.highScoreDynTxt.text = bestScore.toString() // Update high score text
        isSoundEnabled = sharedPreferences?.getBoolean("sound_enabled", true) ?: true

        supportActionBar?.hide()
        startGame()
        continuePassingFocus()

        binding.next.setOnClickListener {
            startGame()
        }

        binding.reset.setOnClickListener {
            resetGame()
        }

        binding.edt15.addTextChangedListener {
            validateRow(
                binding.edt11,
                binding.edt12,
                binding.edt13,
                binding.edt14,
                binding.edt15
            )
        }

        binding.edt25.addTextChangedListener {
            validateRow(
                binding.edt21,
                binding.edt22,
                binding.edt23,
                binding.edt24,
                binding.edt25
            )
        }

        binding.edt35.addTextChangedListener {
            validateRow(
                binding.edt31,
                binding.edt32,
                binding.edt33,
                binding.edt34,
                binding.edt35
            )
        }

        binding.edt45.addTextChangedListener {
            validateRow(
                binding.edt41,
                binding.edt42,
                binding.edt43,
                binding.edt44,
                binding.edt45
            )
        }

        binding.edt55.addTextChangedListener {
            validateRow(
                binding.edt51,
                binding.edt52,
                binding.edt53,
                binding.edt54,
                binding.edt55
            )
        }

        binding.edt65.addTextChangedListener {
            validateRow(
                binding.edt61,
                binding.edt62,
                binding.edt63,
                binding.edt64,
                binding.edt65
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release SoundPool resources
        soundPool.release()
    }

    private fun playCorrectGuessSound() {
        if (isSoundEnabled) {
            soundPool.play(correctGuessSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    private fun playHighScoreSound() {
        if (isSoundEnabled) {
            soundPool.play(highScoreSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    private fun playIncorrectSound() {
        if (isSoundEnabled) {
            soundPool.play(incorrectSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun onBackButtonClick() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

    //function to validate the row
    private fun validateRow(
        edt1: EditText,
        edt2: EditText,
        edt3: EditText,
        edt4: EditText,
        edt5: EditText
    ) {
        val edt1Text = edt1.text.toString()
        val edt2Text = edt2.text.toString()
        val edt3Text = edt3.text.toString()
        val edt4Text = edt4.text.toString()
        val edt5Text = edt5.text.toString()

        val letter1 = currentWord[0].toString()
        val letter2 = currentWord[1].toString()
        val letter3 = currentWord[2].toString()
        val letter4 = currentWord[3].toString()
        val letter5 = currentWord[4].toString()

        // Handle empty inputs
        if (edt1Text.isEmpty() || edt2Text.isEmpty() || edt3Text.isEmpty() || edt4Text.isEmpty() || edt5Text.isEmpty()) {
            resetGame()
            return
        }

        // Handle numeric inputs
        if (edt1Text.toIntOrNull() != null || edt2Text.toIntOrNull() != null || edt3Text.toIntOrNull() != null ||
            edt4Text.toIntOrNull() != null || edt5Text.toIntOrNull() != null
        ) {
            Toast.makeText(this, "Please enter letters only", Toast.LENGTH_SHORT).show()
            resetGame()
            return
        }

        //if the letter includes in the word
        if (edt1Text == letter2 || edt1Text == letter3 || edt1Text == letter4 || edt1Text == letter5) {
            edt1.background = ContextCompat.getDrawable(this, R.drawable.bg_includes)
        }

        if (edt2Text == letter1 || edt2Text == letter3 || edt2Text == letter4 || edt2Text == letter5) {
            edt2.background = ContextCompat.getDrawable(this, R.drawable.bg_includes)
        }

        if (edt3Text == letter2 || edt3Text == letter1 || edt3Text == letter4 || edt3Text == letter5) {
            edt3.background = ContextCompat.getDrawable(this, R.drawable.bg_includes)
        }

        if (edt4Text == letter2 || edt4Text == letter3 || edt4Text == letter1 || edt4Text == letter5) {
            edt4.background = ContextCompat.getDrawable(this, R.drawable.bg_includes)
        }

        if (edt5Text == letter2 || edt5Text == letter3 || edt5Text == letter4 || edt5Text == letter5) {
            edt5.background = ContextCompat.getDrawable(this, R.drawable.bg_includes)
        }

        //if the letter matches
        if (edt1Text == letter1) {
            edt1.background = ContextCompat.getDrawable(this, R.drawable.bg_correct)
        }

        if (edt2Text == letter2) {
            edt2.background = ContextCompat.getDrawable(this, R.drawable.bg_correct)
        }

        if (edt3Text == letter3) {
            edt3.background = ContextCompat.getDrawable(this, R.drawable.bg_correct)
        }

        if (edt4Text == letter4) {
            edt4.background = ContextCompat.getDrawable(this, R.drawable.bg_correct)
        }

        if (edt5Text == letter5) {
            edt5.background = ContextCompat.getDrawable(this, R.drawable.bg_correct)
        }

        if (edt1Text != letter1 && edt1Text != letter2 && edt1Text != letter3 && edt1Text != letter4 && edt1Text != letter5) {
            edt1.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong)
        }

        if (edt2Text != letter1 && edt2Text != letter2 && edt2Text != letter3 && edt2Text != letter4 && edt2Text != letter5) {
            edt2.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong)
        }

        if (edt3Text != letter1 && edt3Text != letter2 && edt3Text != letter3 && edt3Text != letter4 && edt3Text != letter5) {
            edt3.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong)
        }

        if (edt4Text != letter1 && edt4Text != letter2 && edt4Text != letter3 && edt4Text != letter4 && edt4Text != letter5) {
            edt4.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong)
        }

        if (edt5Text != letter1 && edt5Text != letter2 && edt5Text != letter3 && edt5Text != letter4 && edt5Text != letter5) {
            edt5.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong)
        }

        if (edt1Text == letter1 && edt2Text == letter2 && edt3Text == letter3 && edt4Text == letter4 && edt5Text == letter5) {
            Toast.makeText(this, ContextCompat.getString(this, R.string.congrats_txt), Toast.LENGTH_SHORT).show()
            binding.next.visibility = View.VISIBLE
            binding.reset.visibility = View.GONE
            viewModel.increaseScore()
            playCorrectGuessSound()
            updateScoreText()
            checkAndUpdateBestScore()
            makeGameInactive()
            return
        }

        if (edt5.id == R.id.edt_65) {
            Toast.makeText(this, ContextCompat.getString(this, R.string.fail_txt), Toast.LENGTH_SHORT).show()
            binding.next.visibility = View.VISIBLE
            binding.reset.visibility = View.GONE
            checkAndUpdateBestScore()
            playIncorrectSound()
            makeGameInactive()
            return
        }
    }

    private fun updateScoreText() {
        binding.scoreDynTxt.text = viewModel.getScore().toString()
    }

    private fun checkAndUpdateBestScore() {
        if (viewModel.getScore() > bestScore) {
            if (viewModel.getScore() > 0) {
                Toast.makeText(this, "Way to go!", Toast.LENGTH_SHORT).show()
                playHighScoreSound()
            }    
            bestScore = viewModel.getScore()
            sharedPreferences?.edit()?.putInt("best_score", bestScore)?.apply() // Save best score
            binding.highScoreDynTxt.text = bestScore.toString() // Update best score text
        }
    }

    private fun makeGameInactive() {
        binding.edt11.isEnabled = false
        binding.edt12.isEnabled = false
        binding.edt13.isEnabled = false
        binding.edt14.isEnabled = false
        binding.edt15.isEnabled = false
        binding.edt21.isEnabled = false
        binding.edt22.isEnabled = false
        binding.edt23.isEnabled = false
        binding.edt24.isEnabled = false
        binding.edt25.isEnabled = false
        binding.edt31.isEnabled = false
        binding.edt32.isEnabled = false
        binding.edt33.isEnabled = false
        binding.edt34.isEnabled = false
        binding.edt35.isEnabled = false
        binding.edt41.isEnabled = false
        binding.edt42.isEnabled = false
        binding.edt43.isEnabled = false
        binding.edt44.isEnabled = false
        binding.edt45.isEnabled = false
        binding.edt51.isEnabled = false
        binding.edt52.isEnabled = false
        binding.edt53.isEnabled = false
        binding.edt54.isEnabled = false
        binding.edt55.isEnabled = false
        binding.edt61.isEnabled = false
        binding.edt62.isEnabled = false
        binding.edt63.isEnabled = false
        binding.edt64.isEnabled = false
        binding.edt65.isEnabled = false
    }

    //function to maintain a continuous shifting of focus
    private fun continuePassingFocus() {
        //for row 1
        passFocusToNextEditText(binding.edt11, binding.edt12)
        passFocusToNextEditText(binding.edt12, binding.edt13)
        passFocusToNextEditText(binding.edt13, binding.edt14)
        passFocusToNextEditText(binding.edt14, binding.edt15)
        passFocusToNextEditText(binding.edt15, binding.edt21)
        //for row 2
        passFocusToNextEditText(binding.edt21, binding.edt22)
        passFocusToNextEditText(binding.edt22, binding.edt23)
        passFocusToNextEditText(binding.edt23, binding.edt24)
        passFocusToNextEditText(binding.edt24, binding.edt25)
        passFocusToNextEditText(binding.edt25, binding.edt31)
        //for row 3
        passFocusToNextEditText(binding.edt31, binding.edt32)
        passFocusToNextEditText(binding.edt32, binding.edt33)
        passFocusToNextEditText(binding.edt33, binding.edt34)
        passFocusToNextEditText(binding.edt34, binding.edt35)
        passFocusToNextEditText(binding.edt35, binding.edt41)
        //for row 4
        passFocusToNextEditText(binding.edt41, binding.edt42)
        passFocusToNextEditText(binding.edt42, binding.edt43)
        passFocusToNextEditText(binding.edt43, binding.edt44)
        passFocusToNextEditText(binding.edt44, binding.edt45)
        passFocusToNextEditText(binding.edt45, binding.edt51)
        //for row 5
        passFocusToNextEditText(binding.edt51, binding.edt52)
        passFocusToNextEditText(binding.edt52, binding.edt53)
        passFocusToNextEditText(binding.edt53, binding.edt54)
        passFocusToNextEditText(binding.edt54, binding.edt55)
        passFocusToNextEditText(binding.edt55, binding.edt61)
        //for row 5
        passFocusToNextEditText(binding.edt61, binding.edt62)
        passFocusToNextEditText(binding.edt62, binding.edt63)
        passFocusToNextEditText(binding.edt63, binding.edt64)
        passFocusToNextEditText(binding.edt64, binding.edt65)
    }

    //function to shift edit text's focus
    private fun passFocusToNextEditText(
        edt1: EditText,
        edt2: EditText
    ) {
        edt1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(edt: Editable?) {
                if(edt?.length == 1) {
                    edt2.requestFocus()
                }
            }
        })

    }

    private fun startGame() {
        currentWord = dictionary.random() // Pick a random word from the dictionary
        resetEditTextFields()
        binding.edt11.requestFocus() // Set focus on the first EditText
        binding.next.visibility = View.GONE
        binding.reset.visibility = View.VISIBLE
    }

    private fun resetGame() {
        resetEditTextBackgrounds()
        resetEditTextFields()
        binding.edt11.requestFocus()
    }

    // Function to reset EditTexts
    private fun resetEditTextFields() {
        binding.edt11.text?.clear()
        binding.edt12.text?.clear()
        binding.edt13.text?.clear()
        binding.edt14.text?.clear()
        binding.edt15.text?.clear()
        binding.edt21.text?.clear()
        binding.edt22.text?.clear()
        binding.edt23.text?.clear()
        binding.edt24.text?.clear()
        binding.edt25.text?.clear()
        binding.edt31.text?.clear()
        binding.edt32.text?.clear()
        binding.edt32.text?.clear()
        binding.edt33.text?.clear()
        binding.edt34.text?.clear()
        binding.edt35.text?.clear()
        binding.edt41.text?.clear()
        binding.edt42.text?.clear()
        binding.edt43.text?.clear()
        binding.edt44.text?.clear()
        binding.edt45.text?.clear()
        binding.edt51.text?.clear()
        binding.edt52.text?.clear()
        binding.edt53.text?.clear()
        binding.edt54.text?.clear()
        binding.edt55.text?.clear()
        binding.edt61.text?.clear()
        binding.edt62.text?.clear()
        binding.edt63.text?.clear()
        binding.edt64.text?.clear()
        binding.edt65.text?.clear()

        // Enable EditTexts for new game
        binding.edt11.isEnabled = true
        binding.edt12.isEnabled = true
        binding.edt13.isEnabled = true
        binding.edt14.isEnabled = true
        binding.edt15.isEnabled = true
        binding.edt21.isEnabled = true
        binding.edt22.isEnabled = true
        binding.edt23.isEnabled = true
        binding.edt24.isEnabled = true
        binding.edt25.isEnabled = true
        binding.edt31.isEnabled = true
        binding.edt32.isEnabled = true
        binding.edt33.isEnabled = true
        binding.edt34.isEnabled = true
        binding.edt35.isEnabled = true
        binding.edt41.isEnabled = true
        binding.edt42.isEnabled = true
        binding.edt43.isEnabled = true
        binding.edt44.isEnabled = true
        binding.edt45.isEnabled = true
        binding.edt51.isEnabled = true
        binding.edt52.isEnabled = true
        binding.edt53.isEnabled = true
        binding.edt54.isEnabled = true
        binding.edt55.isEnabled = true
        binding.edt61.isEnabled = true
        binding.edt62.isEnabled = true
        binding.edt63.isEnabled = true
        binding.edt64.isEnabled = true
        binding.edt65.isEnabled = true

        resetEditTextBackgrounds()
    }

    private fun resetEditTextBackgrounds() {
        val defaultBackgroundId = R.drawable.bg_edit
        val defaultBackground = ContextCompat.getDrawable(this, defaultBackgroundId)!!
        binding.edt11.background = defaultBackground
        binding.edt12.background = defaultBackground
        binding.edt13.background = defaultBackground
        binding.edt14.background = defaultBackground
        binding.edt15.background = defaultBackground
        binding.edt21.background = defaultBackground
        binding.edt22.background = defaultBackground
        binding.edt23.background = defaultBackground
        binding.edt24.background = defaultBackground
        binding.edt25.background = defaultBackground
        binding.edt31.background = defaultBackground
        binding.edt32.background = defaultBackground
        binding.edt33.background = defaultBackground
        binding.edt34.background = defaultBackground
        binding.edt35.background = defaultBackground
        binding.edt41.background = defaultBackground
        binding.edt42.background = defaultBackground
        binding.edt43.background = defaultBackground
        binding.edt44.background = defaultBackground
        binding.edt45.background = defaultBackground
        binding.edt51.background = defaultBackground
        binding.edt52.background = defaultBackground
        binding.edt53.background = defaultBackground
        binding.edt54.background = defaultBackground
        binding.edt55.background = defaultBackground
        binding.edt61.background = defaultBackground
        binding.edt62.background = defaultBackground
        binding.edt63.background = defaultBackground
        binding.edt64.background = defaultBackground
        binding.edt65.background = defaultBackground
    }

}