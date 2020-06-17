package com.example.flightmobileapp
//import android.support.v8.1.app.AppCompatActivity


import Api
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.SyncStateContract
import android.widget.SeekBar
import android.widget.Toast
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.flightmobileapp.Models.JoyStickData
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.coroutines.coroutineScope
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import java.lang.Math.*
import java.math.RoundingMode
import java.util.*
import kotlin.concurrent.schedule

class GameActivity/*(var url: String)*/ : AppCompatActivity() {
    //private val thread = SimpleRunnable()
    private val client = CommandClient("10.0.2.2", 52686)
    private var joyStick = JoyStickData(0.0, 0.0, 0.0, 0.0)
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        //thread.run(SimulatorView)
        client.connect()
        val thro = findViewById<SeekBar>(R.id.throttle)
        throttle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var x = 1
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                var x = 1
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    joyStick.throttle = (seekBar.progress.toFloat() / 100).toDouble()
                }
                sendValuesToServer()
            }
        })
        val rud = findViewById<SeekBar>(R.id.rudder)
        rud?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var x = 1
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                var x = 1
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    joyStick.rudder = ((seekBar.progress.toFloat() / 100)).toDouble()
                }
                sendValuesToServer()
            }
        })
        coroutineScope {
            getScreenshotFromServer()
        }
/*        Timer().schedule(3000) {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:52686")
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            val api = retrofit.create(Api::class.java)
            val body = api.getImg().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val I = response.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    runOnUiThread {
                        SimulatorView.setImageBitmap(B)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    print("basa")
                }
            })*/
        //val joystick = findViewById(R.id.joystick) as JoystickView
        joystick.setOnMoveListener { angle, strength ->
            val rad = toRadians(angle + 0.0)
            joyStick.aileron = kotlin.math.cos(rad)
            joyStick.elevator = kotlin.math.sin(rad)
            joyStick.aileron = (joyStick.aileron * strength) / 100
            joyStick.elevator = (joyStick.elevator * strength) / 100
            sendValuesToServer()
        }
    }
//}

    /*    object ApiService {
            private val TAG = "--ApiService"

            fun loginApiCall() {

                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val ret = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:52686")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                        (GsonConverterFactory.create(gson))
                            .client(Api.client)
                            .build()
                            .create(API::class.java)!!)
            }
        }*/
    private fun sendValuesToServer() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:52686")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)


        api.createPost(joyStick)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        applicationContext, "Connection failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println("1")
                }
            })
    }

    private suspend fun getScreenshotFromServer() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:52686")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        while (true) {
                Timer().schedule(2000) {
                }
            val body = api.getImg().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val I = response.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    runOnUiThread {
                        SimulatorView.setImageBitmap(B)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    print("basa")
                }
            })
        }
    }
}