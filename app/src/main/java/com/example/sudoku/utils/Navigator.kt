package com.example.sudoku.utils

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}
interface Navigator {

    fun startNewSudoku()

    fun continueSudoku()

    fun goToMenu()

    fun goToSettings()

    fun showEndGameDialog()

    fun restartSudoku()

    fun showStartNewGameDialog()
}