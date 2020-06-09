package com.example.flightmobileapp
//import android.support.v8.1.app.AppCompatActivity


import com.example.flightmobileapp.JoystickView.JoystickListener
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity(), JoystickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val joystick = JoystickView(this)
        setContentView(R.layout.activity_game)
    }

    override fun onJoystickMoved(
        xPercent: Float,
        yPercent: Float,
        id: Int
    ) {
        when (id) {
            R.id.joystickRight -> Log.d(
                "Right Joystick",
                "X percent: $xPercent Y percent: $yPercent"
            )
            R.id.joystickLeft -> Log.d(
                "Left Joystick",
                "X percent: $xPercent Y percent: $yPercent"
            )
        }
    }
}