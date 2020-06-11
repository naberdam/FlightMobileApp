package com.example.flightmobileapp
//import android.support.v8.1.app.AppCompatActivity


import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Math.*


class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val joystick = findViewById(R.id.joystick) as JoystickView
        joystick.setOnMoveListener(object : JoystickView.OnMoveListener {
            override fun onMove(angle: Int, strength: Int) {
                val rad = toRadians(angle + 0.0)
                var x = kotlin.math.cos(rad)
                var y = kotlin.math.sin(rad)
                x = (x * strength) / 100
                y = (y * strength) / 100
                print(x)
                print(y)
            }
        })
    }
}