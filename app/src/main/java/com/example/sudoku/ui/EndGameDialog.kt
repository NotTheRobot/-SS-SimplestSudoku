package com.example.sudoku.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.sudoku.utils.navigator

class EndGameDialog : DialogFragment(), DialogInterface.OnClickListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder =  AlertDialog.Builder(activity)
            .setTitle("ВЫ ПОБЕДИЛИ!")
            .setPositiveButton("Да", this)
            .setNegativeButton("Нет", this)
            .setMessage("Начать новую игру?")
        return dialogBuilder.create()
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        when(p1){
            Dialog.BUTTON_POSITIVE -> {
                navigator().restartSudoku()
            }
            Dialog.BUTTON_NEGATIVE -> {
                navigator().goToMenu()
            }
        }
    }
}