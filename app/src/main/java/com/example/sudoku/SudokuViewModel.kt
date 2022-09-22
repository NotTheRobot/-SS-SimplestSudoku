package com.example.sudoku

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sudoku.data.ItemCellsData
import com.example.sudoku.utils.GeneratingSudoku
import com.example.sudoku.utils.Stopwatch

enum class LaunchSudokuConfig {
    CREATE_NEW,
    CONTINUE
}

class SudokuViewModel: ViewModel() {

    var sudokuGrid = IntArray(81)
    var candidates = Array(81){mutableSetOf<Int>() }
    var solvedGrid = IntArray(81)
    var itemCellsData = ItemCellsData(sudokuGrid, candidates)

    private var _difficulty = MutableLiveData<Int>(20)
    val difficulty: LiveData<Int>
    get() = _difficulty

    var stopwatch = Stopwatch()
    val mistakesCounter = MutableLiveData<Int>(0)
    var launchSudokuConfig: LaunchSudokuConfig? = LaunchSudokuConfig.CREATE_NEW

    fun generateNewSudokuGrid() {
        sudokuGrid = GeneratingSudoku.puzzleSudoku(difficulty.value)
        solvedGrid = GeneratingSudoku.solvedGrid
        candidates = Array(81){mutableSetOf<Int>() }
        itemCellsData.pen = sudokuGrid
        itemCellsData.pencil = candidates
        stopwatch.resetTimer()
        mistakesCounter.value = 0

        var str =""
        for(i in sudokuGrid){
            str += "$i "
        }
        Log.d("lolk", "sudokuGrid: $str")
        str = ""
        for(i in solvedGrid){
            str += "$i "
        }
        Log.d("lolk", "solvedGrid: $str")
    }

    fun addDifficulty(){
        if(_difficulty.value!! < 45){
            _difficulty.value = _difficulty.value!!.plus(1)
        }
    }

    fun removeDifficulty(){
        if(_difficulty.value!! > 1){
            _difficulty.value = _difficulty.value!!.plus(-1)
        }
    }

    fun isRight(number: Int, position: Int): Boolean{
        return solvedGrid[position] == number
    }

    fun onCandidateEntered(number: Int, position: Int){
        if(number in candidates[position]){
            removeCandidate(number, position)
        }
        else{
            addCandidate(number, position)
        }
    }
    private fun addCandidate(number: Int, position: Int){
        candidates[position].add(number)
    }
    private fun removeCandidate(number: Int, position: Int){
        candidates[position].remove(number)
    }
    fun addNumber(number: Int, position: Int){
        sudokuGrid[position] = number
    }

    fun clearCandidates(position: Int){
        candidates[position].clear()
    }

    fun isSolved(): Boolean{
        for(i in sudokuGrid){
            if(i == 0)
            {
                return false
            }
        }
        return true
    }

    fun addMistake(){
        mistakesCounter.value = mistakesCounter.value!!.plus(1)
    }
}

