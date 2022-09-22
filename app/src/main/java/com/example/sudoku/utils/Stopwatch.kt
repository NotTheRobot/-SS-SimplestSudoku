package com.example.sudoku.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.io.Serializable
import java.text.SimpleDateFormat


class Stopwatch(): Serializable {

    private var handler: Handler? = null
    private var timer = 0L
    val showTimer = MutableLiveData<String>("00:00:00")
    private var startTime = 0L
    private var stopTime = 0L
    val stopTimeReader: Long
    get() = stopTime

    private var statusChecker = object : Runnable{
        override fun run() {
            val interval = 1000L
            try {
                timer = stopTime + System.currentTimeMillis() - startTime
                refreshShowTimer()
            }
            finally {
                handler!!.postDelayed(this, interval)
            }
        }
    }

    fun startTimer() {
        startTime = System.currentTimeMillis()
        timer = stopTime
        handler = Handler(Looper.getMainLooper())
        statusChecker.run()
    }

    fun stopTimer() {
        stopTime = timer
        handler!!.removeCallbacks(statusChecker)
    }

    fun resetTimer() {
        startTime = 0L
        timer = 0L
        stopTime = 0L
        refreshShowTimer()

    }

    fun setStopTime(time: Long){
        stopTime = time
    }

    private fun refreshShowTimer(){
        Log.d("tag", "refreshingShowTimer")
        val hours = timer / 3600000
        val minutes = (timer / 60000) % 60
        val seconds = (timer / 1000) % 60

        val sdf = SimpleDateFormat("HH:mm:ss").parse("${hours}:$minutes:$seconds:")
        showTimer.value = SimpleDateFormat("HH:mm:ss").format(sdf)
    }
}