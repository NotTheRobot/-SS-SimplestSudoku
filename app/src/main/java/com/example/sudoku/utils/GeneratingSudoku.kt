package com.example.sudoku.utils

import android.util.Log

fun Array<IntArray>.copy() = Array(size){get(it).clone()}

class GeneratingSudoku() {
    companion object{
        var solvedGrid = IntArray(81)

        fun puzzleSudoku(difficulty: Int?): IntArray{
            if(difficulty == null){
                return IntArray(81)
            }
            var sudokuGrid = generateNewSudokuGrid()
            for(i in 0..8){
                for(j in 0..8){
                    solvedGrid[9 * i + j] = sudokuGrid[i][j]
                }
            }
            var rowOfLastDeleted: Int
            var columnOfLastDeleted: Int
            var i = 0
            var valueOfLastDeleted = 0
            var countOfUnresolvedIterations = 0
            var puzzledGrid = IntArray(81)

            while(i < difficulty) {
                rowOfLastDeleted = (Math.random() * 9).toInt()
                columnOfLastDeleted = (Math.random() * 9).toInt()

                if(i % 2 == 0){
                    rowOfLastDeleted = findTheLongestRow(sudokuGrid)
                    while(sudokuGrid[rowOfLastDeleted][columnOfLastDeleted] == 0 || isRowOrColumnEmpty(sudokuGrid, rowOfLastDeleted, columnOfLastDeleted)) {
                        columnOfLastDeleted = (Math.random() * 9).toInt()
                    }
                } else {
                    columnOfLastDeleted = findTheLongestColumn(sudokuGrid)
                    while(sudokuGrid[rowOfLastDeleted][columnOfLastDeleted] == 0 || isRowOrColumnEmpty(sudokuGrid, rowOfLastDeleted, columnOfLastDeleted)) {
                        rowOfLastDeleted = (Math.random() * 9).toInt()
                    }
                }

                valueOfLastDeleted = sudokuGrid[rowOfLastDeleted][columnOfLastDeleted]
                sudokuGrid[rowOfLastDeleted][columnOfLastDeleted] = 0

                if(isSolvable(sudokuGrid)) {
                    i++
                    countOfUnresolvedIterations = 0
                } else {
                    sudokuGrid[rowOfLastDeleted][columnOfLastDeleted] = valueOfLastDeleted
                    countOfUnresolvedIterations++
                    if(countOfUnresolvedIterations >= 81){
                        i = 0
                        countOfUnresolvedIterations = 0
                        sudokuGrid = generateNewSudokuGrid()

                        for(i in 0..8){
                            for(j in 0..8){
                                solvedGrid[9 * i + j] = sudokuGrid[i][j]
                            }
                        }
                    }
                }
            }

            for(i in 0..8){
                for(j in 0..8){
                    puzzledGrid[9 * i + j] = sudokuGrid[i][j]
                }
            }
            return puzzledGrid
        }

        private val startGrid = arrayOf(
            intArrayOf(1,2,3,4,5,6,7,8,9),
            intArrayOf(4,5,6,7,8,9,1,2,3),
            intArrayOf(7,8,9,1,2,3,4,5,6),
            intArrayOf(2,3,4,5,6,7,8,9,1),
            intArrayOf(5,6,7,8,9,1,2,3,4),
            intArrayOf(8,9,1,2,3,4,5,6,7),
            intArrayOf(3,4,5,6,7,8,9,1,2),
            intArrayOf(6,7,8,9,1,2,3,4,5),
            intArrayOf(9,1,2,3,4,5,6,7,8)
        )

        private fun generateNewSudokuGrid(): Array<IntArray>{
            val newSudokuGrid = startGrid.copy()
            var secondColumn = 0
            var secondRow = 0
            for(i in 0..6){
                for(firstColumn in 0..8){
                    when (firstColumn) {
                        in 0..2 -> secondColumn = (Math.random() * 3).toInt()
                        in 3..5 -> secondColumn = 3 + (Math.random() * 3).toInt()
                        in 6..8 -> secondColumn = 6 + (Math.random() * 3).toInt()
                    }

                    if(firstColumn != secondColumn){
                        swapColumn(newSudokuGrid, firstColumn, secondColumn)
                    }
                }

                for(firstRow in 0..8){
                    when (firstRow) {
                        in 0..2 -> secondRow = (Math.random() * 3).toInt()
                        in 3..5 -> secondRow = 3 + (Math.random() * 3).toInt()
                        in 6..8 -> secondRow = 6 + (Math.random() * 3).toInt()
                    }

                    if(firstRow != secondRow){
                        swapRow(newSudokuGrid, firstRow, secondRow)
                    }
                }
            }
            return newSudokuGrid
        }

        private fun swapColumn(grid: Array<IntArray>, firstColumn: Int, secondColumn: Int){
            var temp: Int
            for(i in 0..8){
                temp = grid[i][firstColumn]
                grid[i][firstColumn] = grid[i][secondColumn]
                grid[i][secondColumn] = temp
            }
        }

        private fun swapRow(grid: Array<IntArray>, firstRow: Int, secondRow: Int){
            var temp: Int
            for(i in 0..8){
                temp = grid[firstRow][i]
                grid[firstRow][i] = grid[secondRow][i]
                grid[secondRow][i] = temp
            }
        }

        private fun isSolvable(sudokuGrid: Array<IntArray>): Boolean {
            Log.d("kekw", "isSolvable started")
            val candidates = Array(9){
                Array(9){
                    mutableSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
                }
            }
            var solvable = false
            val grid = sudokuGrid.copy()

            if (isSolved(grid)) {
                return true
            }

            //filtering candidates
            for (i in 0..8) {
                for (j in 0..8) {
                    if (grid[i][j] == 0) {
                        checkArea(grid, candidates, i, j)
                        checkRowAndColumn(grid, candidates, i, j)
                    } else {
                        candidates[i][j].clear()
                    }
                }
            }
            var result = true

            //solving sudoku
            while (true) {
                solvable = false
                for (i in 0..8) {
                    for (j in 0..8) {
                        if (candidates[i][j].isNotEmpty()) {
                            if (candidates[i][j].size == 1) {
                                grid[i][j] = candidates[i][j].toIntArray()[0]
                                candidates[i][j].clear()
                                deleteSolvedCandidate(grid[i][j], candidates, i, j)
                                solvable = true
                            } else if (isThereUniqueCandidate(candidates, i, j) != 0) {
                                grid[i][j] = isThereUniqueCandidate(candidates, i, j)
                                candidates[i][j].clear()
                                deleteSolvedCandidate(grid[i][j], candidates, i, j)
                                solvable = true
                            }
                        }
                    }
                }
                if (isSolved(grid)) {
                    checkSolvedGrid(grid)
                    result = true
                    break
                } else if (!solvable) {
                    result = false
                    break
                }
            }
            return result
        }

        private fun checkArea(grid: Array<IntArray>, candidates: Array<Array<MutableSet<Int>>>, i: Int, j: Int){
            val x = i - i % 3
            val y = j - j % 3

            for(k in x..x + 2){
                for(l in y..y + 2){
                    candidates[i][j].remove(grid[k][l])
                }
            }
        }

        private fun checkRowAndColumn(grid: Array<IntArray>, candidates: Array<Array<MutableSet<Int>>>, i: Int, j: Int){
            for(k in 0..8){
                candidates[i][j].remove(grid[i][k])
                candidates[i][j].remove(grid[k][j])
            }
        }

        private fun isThereUniqueCandidate(candidates: Array<Array<MutableSet<Int>>>, i: Int, j: Int): Int{
            val unionOfSet = mutableSetOf<Int>()

            for(k in 0..8){
                unionOfSet.addAll(candidates[k][j])
                unionOfSet.addAll(candidates[i][k])
            }

            val x = i - i % 3
            val y = j - j % 3
            for(k in x..x + 2){
                for(l in y..y + 2){
                    candidates[i][j].addAll(candidates[k][l])
                }
            }

            if(candidates[i][j].subtract(unionOfSet).size == 1){
                return candidates[i][j].subtract(unionOfSet).toIntArray()[0]
            } else{
                return 0
            }
        }

        private fun isSolved(sudokuGrid: Array<IntArray>): Boolean{

            for (i in 0..8) {
                for (j in 0..8) {
                    if (sudokuGrid[i][j] == 0) {
                        return false
                    }
                }
            }
            return true
        }

        private fun deleteSolvedCandidate(solvedCandidate: Int, candidates: Array<Array<MutableSet<Int>>>, i: Int, j: Int){
            for(k in 0..8){
                candidates[k][j].remove(solvedCandidate)
                candidates[i][k].remove(solvedCandidate)
            }

            val x = i - i % 3
            val y = j - j % 3
            for(k in x..x + 2){
                for(l in y..y + 2){
                    candidates[k][l].remove(solvedCandidate)
                }
            }
        }

        private fun isRowOrColumnEmpty(grid: Array<IntArray>, i: Int, j: Int ): Boolean{
            var isRowEmpty = true
            var isColumnEmpty = true
            for(k in 0..8){
                if(grid[i][k] > 0 && k != j){
                    isRowEmpty = false
                }
                if(grid[k][j] > 0 && k != i){
                    isColumnEmpty = false
                }
            }
            return isRowEmpty || isColumnEmpty
        }

        private fun findTheLongestRow(sudokuGrid: Array<IntArray>): Int{
            var longestRow = 0
            var countOfNumbersInRow = 0
            var maxCountOfNumbersInRow = 0
            for(i in 0..8){
                countOfNumbersInRow = 0
                for(j in 0..8){
                    if(sudokuGrid[i][j] >= 1){
                        countOfNumbersInRow++
                    }
                }
                if(countOfNumbersInRow > maxCountOfNumbersInRow){
                    longestRow = i
                    maxCountOfNumbersInRow = countOfNumbersInRow
                }
            }
            return longestRow
        }

        private fun findTheLongestColumn(sudokuGrid: Array<IntArray>): Int{
            var longestColumn = 0
            var countOfNumbersInColumn = 0
            var maxCountOfNumbersInColumn = 0
            for(i in 0..8){
                countOfNumbersInColumn = 0
                for(j in 0..8){
                    if(sudokuGrid[j][i] >= 1){
                        countOfNumbersInColumn++
                    }
                }
                if(countOfNumbersInColumn > maxCountOfNumbersInColumn){
                    longestColumn = i
                    maxCountOfNumbersInColumn = countOfNumbersInColumn
                }
            }
            return longestColumn
        }

        private fun checkSolvedGrid(grid: Array<IntArray>){
            val checker = setOf(1,2,3,4,5,6,7,8,9)
            for(i in 0..8){
                if(checker.subtract(grid[i].toSet()) != setOf<Int>()){
                    throw Exception("Wrong Answer")
                }
            }
        }
    }
}