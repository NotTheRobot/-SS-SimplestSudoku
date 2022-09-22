package com.example.sudoku

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.example.sudoku.databinding.ActivityMainBinding
import com.example.sudoku.ui.*
import com.example.sudoku.utils.Navigator

class MainActivity : AppCompatActivity(), Navigator {

    val PREF_KEY = "Saving_state"

    private val sharedViewModel: SudokuViewModel by viewModels()
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        _binding.lifecycleOwner = this
        restoreState()
    }

    override fun onStop() {
        Log.d("lolk", "MainActivity onStop")
        super.onStop()
        saveData()
    }


    override fun startNewSudoku() {
        Log.d("kekw", "goToSudoku started")
        sharedViewModel.launchSudokuConfig = LaunchSudokuConfig.CREATE_NEW
        supportFragmentManager.commit {
            replace(R.id.fragment_container, TheSudokuFragment())
            addToBackStack(null)
        }
    }

    override fun continueSudoku() {
        Log.d("kekw", "goToSudoku started")
        sharedViewModel.launchSudokuConfig = LaunchSudokuConfig.CONTINUE
        supportFragmentManager.commit {
            replace(R.id.fragment_container, TheSudokuFragment())
            addToBackStack(null)
        }
    }

    override fun goToMenu() {
        supportFragmentManager.popBackStack()
    }

    override fun goToSettings() {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, SettingsFragment())
            addToBackStack(null)
        }
    }

    override fun showEndGameDialog() {
        val dialogFragment = EndGameDialog()
        dialogFragment.isCancelable = false
        dialogFragment.show(supportFragmentManager, "endGameDialog")
    }

    override fun showStartNewGameDialog(){
        val dialogFragment = StartNewGameDialog()
        dialogFragment.show(supportFragmentManager, "startNewGameDialog")
    }

    override fun restartSudoku() {
        supportFragmentManager.popBackStack()
        startNewSudoku()
    }

    private fun saveData(){
        val sharedPreferences = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        Log.d("cho", "saveData started")
        with(sharedPreferences.edit()){
            putString("sudokuGrid", sharedViewModel.sudokuGrid.contentToString())
            putString("solvedGrid", sharedViewModel.solvedGrid.contentToString())
            putString("candidates", sharedViewModel.candidates.contentToString())
            putInt("mistakesCounter", sharedViewModel.mistakesCounter.value!!)
            putLong("lastStopTimer", sharedViewModel.stopwatch.stopTimeReader)
            apply()
        }
    }

    private fun restoreState(){
        val sharedPreferences = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        Log.d("cho", "restoreState started")
        with(sharedViewModel){
            sudokuGrid = sharedPreferences.getString("sudokuGrid", "")!!.toIntArray()
            solvedGrid = sharedPreferences.getString("solvedGrid", "")!!.toIntArray()
            stopwatch.setStopTime(sharedPreferences.getLong("lastStopTimer", 0L))
            mistakesCounter.value = sharedPreferences.getInt("mistakesCounter", 0)
            candidates = sharedPreferences.getString("candidates", "")!!.toMutableIntSet()
            itemCellsData.pen = sharedViewModel.sudokuGrid
            itemCellsData.pencil = sharedViewModel.candidates
        }
    }

    private fun String.toIntArray(): IntArray{

        var items = this.replace("[", "").replace("]","").split(", ")
        val result = IntArray(items.size)

        for(i in items.indices){
            result[i] = items[i].toInt()
        }
        return result
    }

    private fun String.toMutableIntSet(): Array<MutableSet<Int>>{
        var items = this.substring(2, this.length - 2).split("], [").toTypedArray()
        var result = Array(items.size){ mutableSetOf<Int>()}
        for(i in items.indices){
            if(items[i] != ""){
                result[i] = items[i].toIntArray().toMutableSet()
            }
        }
        return result
    }
}