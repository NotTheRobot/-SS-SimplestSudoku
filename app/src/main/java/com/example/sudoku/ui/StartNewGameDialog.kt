package com.example.sudoku.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.sudoku.utils.navigator

class StartNewGameDialog: DialogFragment(), DialogInterface.OnClickListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(activity)
            .setTitle("Внимание")
            .setMessage("Вы уверены, что хотите начать новую игру? Предыдущая игра будет удалена")
            .setNegativeButton("Нет", this)
            .setPositiveButton("Да", this)
        return dialogBuilder.create()
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {

        when(p1){
            Dialog.BUTTON_POSITIVE -> {
                navigator().startNewSudoku()
            }
            else ->{

            }
        }
    }
}