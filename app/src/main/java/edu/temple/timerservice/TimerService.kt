package edu.temple.timerservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log

class TimerService : Service() {

    var paused = false
    var shouldExit = false
    lateinit var timerHandler : Handler

    inner class TimerBinder : Binder() {
        fun start10SecondTimer() {
            runTimer(10)
        }
        fun start1MinuteTimer() {
            runTimer(60)
        }
        fun start1HourTimer () {
            runTimer(3600)
        }

        fun pauseTimer(): Boolean {
            paused = !paused
            return paused
        }

        fun setHandler(handler: Handler) {
            timerHandler = handler
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return TimerBinder()
    }

    fun runTimer(startTime : Int) {
        Thread {
            for (i in startTime downTo 1) {
                if (shouldExit) break
                Log.d("Countdown", i.toString())
                if (::timerHandler.isInitialized)
                    timerHandler.sendEmptyMessage(i)
                Thread.sleep(1000)
                while(paused);
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        shouldExit = true
        paused = false
    }
}