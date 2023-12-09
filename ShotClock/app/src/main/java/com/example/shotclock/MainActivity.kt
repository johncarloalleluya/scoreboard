package com.example.shotclock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.media.MediaPlayer


class MainActivity : AppCompatActivity() {

    // Create a MediaPlayer instance
    private var buzzerMediaPlayer: MediaPlayer? = null



    private var teamAScore = 0
    private var teamBScore = 0
    private var shotClock = 24
    private var timerClock = 600 //10 minutes ( 10 * 60 seconds)
    private var currentPeriod = 1
    private var currentFoul = 0

    private var shotClockIsRunning = false
    private var shotClockTimer: CountDownTimer? = null

    private var timerClockIsRunning = false
    private var timerClockTimer: CountDownTimer? = null


    private fun playBuzzerSound() {
        buzzerMediaPlayer?.start()
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the buzzer MediaPlayer
        buzzerMediaPlayer = MediaPlayer.create(this, R.raw.buzzer_sound)

        buzzerMediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }

        try {
            buzzerMediaPlayer = MediaPlayer.create(this, R.raw.buzzer_sound)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Add this inside your onCreate method after initializing other views
        val buzzerButton = findViewById<Button>(R.id.buzzerButton)
        buzzerButton.setOnClickListener {
            playBuzzerSound()
        }


        val teamAScoreTextView = findViewById<TextView>(R.id.teamAScore)
        val teamBScoreTextView = findViewById<TextView>(R.id.teamBScore)
        val shotClockTextView = findViewById<TextView>(R.id.shotClock)
        val timerClockTextView = findViewById<TextView>(R.id.timerClock)
        val periodTextView = findViewById<TextView>(R.id.period)
        val foulATextView = findViewById<TextView>(R.id.foulA)
        val foulBTextView = findViewById<TextView>(R.id.foulB)
        val resetShotClockButton = findViewById<Button>(R.id.resetShotClockButton)
        resetShotClockButton.setOnClickListener {
            resetShotClock()
        }


        // Set the initial text for the timerClockTextView
        timerClockTextView.text = " ${timerClock / 60}:${String.format("%02d", timerClock % 60)}"
        periodTextView.text = "$currentPeriod"
        foulATextView.text = "$currentFoul"
        foulBTextView.text ="$currentFoul"

        val increaseTeamAScoreButton = findViewById<Button>(R.id.increaseTeamAScore)
        val increaseTeamBScoreButton = findViewById<Button>(R.id.increaseTeamBScore)
        val decreaseTeamAScoreButton = findViewById<Button>(R.id.decreaseTeamAScore)
        val decreaseTeamBScoreButton = findViewById<Button>(R.id.decreaseTeamBScore)
        val increaseMinutesButton = findViewById<Button>(R.id.increaseMinutesButton)
        val decreaseMinutesButton = findViewById<Button>(R.id.decreaseMinutesButton)
        val increaseSecondsButton = findViewById<Button>(R.id.increaseSecondsButton)
        val decreaseSecondsButton = findViewById<Button>(R.id.decreaseSecondsButton)




        increaseMinutesButton.setOnClickListener {
            increaseMinutes()
        }

        decreaseMinutesButton.setOnClickListener {
            decreaseMinutes()
        }

        increaseSecondsButton.setOnClickListener {
            increaseSeconds()
        }

        decreaseSecondsButton.setOnClickListener {
            decreaseSeconds()
        }


        increaseTeamAScoreButton.setOnClickListener {
            teamAScore++
            teamAScoreTextView.text = "$teamAScore"
            teamAScoreTextView.text = String.format("%02d", teamAScore)
        }

        increaseTeamBScoreButton.setOnClickListener {
            teamBScore++
            teamBScoreTextView.text = "$teamBScore"
            teamBScoreTextView.text = String.format("%02d", teamBScore)

        }

        decreaseTeamAScoreButton.setOnClickListener {
            if (teamAScore > 0) {
                teamAScore--
                teamAScoreTextView.text = "$teamAScore"
                teamAScoreTextView.text = String.format("%02d", teamAScore)
            }
        }

        decreaseTeamBScoreButton.setOnClickListener {
            if (teamBScore > 0) {
                teamBScore--
                teamBScoreTextView.text = "$teamBScore"
                teamBScoreTextView.text = String.format("%02d", teamBScore)
            }
        }

        shotClockTextView.setOnClickListener {
            if (shotClockIsRunning) {
                // Pause the shot clock
                shotClockTimer?.cancel()
                shotClockIsRunning = false
                timerClockTimer?.cancel()
                timerClockIsRunning = false
            } else {
                // Start the shot clock countdown
                startShotClock(shotClock, shotClockTextView)
                shotClockIsRunning = true
                startTimerClock(timerClock, timerClockTextView)
                timerClockIsRunning = true
            }

        }


        timerClockTextView.setOnClickListener {
            if (timerClockIsRunning) {
                // Pause the timer clock
                timerClockTimer?.cancel()
                timerClockIsRunning = false
                shotClockTimer?.cancel()
                shotClockIsRunning = false
                } else {
                // Start the timer clock countdown
                startTimerClock(timerClock, timerClockTextView)
                timerClockIsRunning = true
                startShotClock(shotClock, shotClockTextView)
                shotClockIsRunning = true
            }
        }
        periodTextView.setOnClickListener {
            // Increment the period
            if (currentPeriod < 4) {
                currentPeriod++
            } else {
                // If the current period is 4, loop back to 1
                currentPeriod = 1
            }
            periodTextView.text = "$currentPeriod"
        }

        foulATextView.setOnClickListener {
            // Increment the period
            if (currentFoul < 5) {
                currentFoul++
            } else {
                // If the current period is 5, loop back to 0
                currentFoul = 0
            }
            foulATextView.text = "$currentFoul"

        }

        foulBTextView.setOnClickListener {
            // Increment the period
            if (currentFoul < 5) {
                currentFoul++
            } else {
                // If the current period is 5, loop back to 0
                currentFoul = 0
            }
            foulBTextView.text = "$currentFoul"

        }

    }




    private fun increaseMinutes() {
        timerClockTimer?.cancel() // Stop the timer
        shotClockTimer?.cancel()  // Stop the shot clock
        timerClock += 60 // Increase timer by 60 seconds (1 minute)
        shotClock = 24 // Reset the shot clock to 24 seconds
        updateTimerTextView()
       
    }

    private fun decreaseMinutes() {
        timerClockTimer?.cancel() // Stop the timer
        shotClockTimer?.cancel()  // Stop the shot clock
        timerClock -= 60 // Decrease timer by 60 seconds (1 minute)
        shotClock = 24 // Reset the shot clock to 24 seconds
        updateTimerTextView()

    }

    private fun increaseSeconds() {
        timerClockTimer?.cancel() // Stop the timer
        shotClockTimer?.cancel()  // Stop the shot clock
        timerClock += 1 // Increase timer by 1 second
        shotClock = 24 // Reset the shot clock to 24 seconds
        updateTimerTextView()

    }


    private fun decreaseSeconds() {
        timerClockTimer?.cancel() // Stop the timer
        shotClockTimer?.cancel()  // Stop the shot clock
        timerClock -= 1 // Decrease timer by 1 second
        shotClock = 24 // Reset the shot clock to 24 seconds
        updateTimerTextView()

    }


    private fun updateTimerTextView() {
        val timerClockTextView = findViewById<TextView>(R.id.timerClock)
        val minutes = timerClock / 60
        val seconds = timerClock % 60
        timerClockTextView.text = "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
    }

    override fun onDestroy() {
        // Release the MediaPlayer resources
        buzzerMediaPlayer?.release()
        super.onDestroy()
    }

    private fun resetShotClock() {
        // Reset the shot clock value to 24 seconds
        shotClock = 24

        // Update the shot clock text view
        val shotClockTextView = findViewById<TextView>(R.id.shotClock)
        shotClockTextView.text = "$shotClock"

        // If the shot clock was running, cancel the existing countdown timer
        shotClockTimer?.cancel()
        shotClockIsRunning = false

        // Start the shot clock countdown
        startShotClock(shotClock, shotClockTextView)
        shotClockIsRunning = true

        // If the timer clock is not running, start it
        if (!timerClockIsRunning) {
            val timerClockTextView = findViewById<TextView>(R.id.timerClock)
            startTimerClock(timerClock, timerClockTextView)
            timerClockIsRunning = true
        }
    }


    private fun startShotClock(shotClockValue: Int, shotClockTextView: TextView) {
        shotClockTimer = object : CountDownTimer((shotClockValue * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                shotClock = (millisUntilFinished / 1000).toInt()
                shotClockTextView.text = "$shotClock"
                if (shotClock == 0) {
                    // Shot clock reached 0, play the buzzer sound
                    buzzerMediaPlayer?.start()

                    // Pause the timer
                    timerClockTimer?.cancel()
                    timerClockIsRunning = false
                }
            }

            override fun onFinish() {
                shotClock = 0
                shotClockTextView.text = "$shotClock"
            }
        }
        shotClockTimer?.start()
    }

    private fun startTimerClock(timerClockValue: Int, timerClockTextView: TextView) {
        timerClockTimer = object : CountDownTimer((timerClockValue * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerClock = (millisUntilFinished / 1000).toInt()
                val minutes = timerClock / 60
                val seconds = timerClock % 60
                timerClockTextView.text = "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
                if (timerClock == 0) {
                    // Shot clock reached 0, pause the timer
                    shotClockTimer?.cancel()
                    shotClockIsRunning = false
                }
            }



            override fun onFinish() {
                shotClock = 0
                timerClockTextView.text = "00:00"
            }
        }
        timerClockTimer?.start()
    }
}