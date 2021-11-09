package edu.temple.timerservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {



    var isConnected = false
    lateinit var timerBinder: TimerService.TimerBinder

    lateinit var startTimerButton : Button
    lateinit var pauseButton: Button
    lateinit var countdownTextView: TextView

    val timerHandler = Handler(Looper.getMainLooper()) {
        countdownTextView.text = it.what.toString()
        true
    }

    val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isConnected = true
            timerBinder = service as TimerService.TimerBinder
            timerBinder.setHandler(timerHandler)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTimerButton = findViewById(R.id.startTimerButton)
        pauseButton = findViewById(R.id.pauseTimerButton)
        countdownTextView = findViewById(R.id.countdownTimerTextView)

        startTimerButton.setOnClickListener {
            if (isConnected) timerBinder.start1MinuteTimer()
        }

        pauseButton.setOnClickListener {
            if (isConnected) {
                if (timerBinder.pauseTimer())
                    pauseButton.text = "Resume"
                else
                    pauseButton.text = "Pause"
            }
        }

        bindService(Intent(this, TimerService::class.java)
            , serviceConnection
            , BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}