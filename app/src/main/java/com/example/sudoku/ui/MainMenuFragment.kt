package com.example.sudoku.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.sudoku.SudokuViewModel
import com.example.sudoku.databinding.FragmentMainMenuBinding
import com.example.sudoku.utils.navigator


class MainMenuFragment : Fragment() {

    private val sharedViewModel: SudokuViewModel by activityViewModels()
    private lateinit var binding: FragmentMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainMenuBinding.inflate(layoutInflater, container, false)
        if(sharedViewModel.isSolved()){
            binding.continueGameButton.visibility = View.GONE
        }
        else{
            binding.continueGameButton.visibility = View.VISIBLE
        }
        binding.mainMenuFragment = this
        return binding.root
    }
    fun launchNewSudoku(){
        navigator().showStartNewGameDialog()
    }

    fun continueSudoku(){
        navigator().continueSudoku()
    }

    fun goToSettings(){
        navigator().goToSettings()
    }
}