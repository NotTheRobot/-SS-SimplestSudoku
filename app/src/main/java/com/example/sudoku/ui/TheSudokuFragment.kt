package com.example.sudoku.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.sudoku.*
import com.example.sudoku.databinding.FragmentTheSudokuBinding
import com.example.sudoku.utils.navigator

enum class PenOrPencil{
    PEN,
    PENCIL
}

class TheSudokuFragment : Fragment(), OnItemClickListener {

    private val sharedViewModel: SudokuViewModel by activityViewModels()
    private var binding: FragmentTheSudokuBinding? = null
    private var currentItem: TextView? = null
    private var currentPosition: Int? = null
    private var penOrPencil = PenOrPencil.PEN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (sharedViewModel.launchSudokuConfig == LaunchSudokuConfig.CREATE_NEW) {
            sharedViewModel.generateNewSudokuGrid()
            sharedViewModel.launchSudokuConfig = null
        } else if(sharedViewModel.launchSudokuConfig == LaunchSudokuConfig.CONTINUE) {
            sharedViewModel.launchSudokuConfig = null
        }

        binding = FragmentTheSudokuBinding.inflate(layoutInflater, container, false)
        val recyclerViewAdapter =
            GridAdapter(requireContext(),
               sharedViewModel.itemCellsData, this)
        val gridLayoutManager = StaggeredGridLayoutManager(9, StaggeredGridLayoutManager.VERTICAL)
        with(binding!!.recyclerView) {
            layoutManager = gridLayoutManager
            adapter = recyclerViewAdapter
        }

        binding!!.viewModel = sharedViewModel
        binding!!.sudokuFragment = this
        binding!!.lifecycleOwner = this

        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.stopwatch.startTimer()
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.stopwatch.stopTimer()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onItemClicked(view: TextView, position: Int) {
        currentItem?.setBackgroundColor(Color.argb(100, 255, 255, 255))
        view!!.setBackgroundColor(Color.argb(100,125,150,220))
        currentItem = view
        currentPosition = position
    }

    fun changeMode(){
        if(penOrPencil == PenOrPencil.PEN)
        {
            binding!!.changeModeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pencil))
            binding!!.modeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.pencil))
            binding!!.modeTextView.text = "PENCIL"
            penOrPencil = PenOrPencil.PENCIL

        }
        else {
            binding!!.changeModeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pen))
            binding!!.modeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.pen))
            binding!!.modeTextView.text = "PEN"
            penOrPencil = PenOrPencil.PEN

        }
    }

    fun onEnterNumber(number: Int){
        if(currentPosition != null && sharedViewModel.sudokuGrid[currentPosition!!] == 0){
            if(penOrPencil == PenOrPencil.PENCIL){
                currentItem!!.setModePencil()
                sharedViewModel.onCandidateEntered(number, currentPosition!!)
                binding!!.recyclerView.adapter!!.notifyItemChanged(currentPosition!!, penOrPencil)
            }
            else if(sharedViewModel.isRight(number, currentPosition!!)){
                currentItem!!.setModePen()
                currentItem!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.pen))
                sharedViewModel.addNumber(number, currentPosition!!)
                binding!!.recyclerView.adapter!!.notifyItemChanged(currentPosition!!, penOrPencil)
                if(sharedViewModel.isSolved()){
                    launchEndGameDialog()
                    sharedViewModel.stopwatch.resetTimer()
                }
            }
            else{
                sharedViewModel.addMistake()
            }
        }
    }

    private fun launchEndGameDialog(){
        navigator().showEndGameDialog()
    }

    private fun TextView.setModePen(){
        sharedViewModel.clearCandidates(currentPosition!!)
        val pencilDimen = 20f
        currentItem!!.textSize = pencilDimen
        currentItem!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.pen))
    }

    private fun TextView.setModePencil(){
        val pencilDimen = 9.5f
        currentItem!!.textSize = pencilDimen
        currentItem!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.pencil))
    }

    fun showPrompt(){
        if(!sharedViewModel.isSolved()) {
            var position = (Math.random() * 81).toInt()
            while (sharedViewModel.sudokuGrid[position] != 0) {
                position = (Math.random() * 81).toInt()
            }

            val item = binding!!.recyclerView.layoutManager!!.getChildAt(position)
            onItemClicked(item!!.findViewById(R.id.cell_text), position)
            onEnterNumber(sharedViewModel.solvedGrid[position])
        }
    }
}