package com.warclips.wordzilla

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    //mutable UI related data
    private var score = 0

    fun getScore(): Int {
        return score
    }

    fun increaseScore() {
        score += 10
    }
}