package com.example.flightmobileapp
//import android.support.v8.1.app.AppCompatActivity


import Api
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.flightmobileapp.Models.JoyStickData
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.*

class GameActivity/*(var url: String)*/ : AppCompatActivity() {
    //handler that handle the screenshot image every 700ms
    private lateinit var mainHandler: Handler
    private lateinit var url:String
    private var ip:String = ""
    private var port:Int = 0
    private var cont:Context = this
    private val updateTextTask = object : Runnable {
        override fun run() {
            getScreenshotFromServer()
            mainHandler.postDelayed(this, 450)
        }
    }
    //private val client = CommandClient(ip, port)
    private var joyStick = JoyStickData(0.0, 0.0, 0.0, 0.0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent.getStringExtra("url")!=null) {
            url = intent.getStringExtra("url")
        }
        setContentView(R.layout.activity_game)
        getScreenshotFromServer()
        convertFromUrlToIpAndPort();
        //client.connect()
        mainHandler = Handler(Looper.getMainLooper())
        throttle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                print("check1")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                print("check1")
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
                print("check1")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                print("check1")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    joyStick.rudder = ((seekBar.progress.toFloat() / 100)).toDouble()
                }
                sendValuesToServer()
            }
        })
        joystick.setOnMoveListener { angle, strength ->
            val rad = toRadians(angle + 0.0)
            joyStick.aileron = kotlin.math.cos(rad)
            joyStick.elevator = kotlin.math.sin(rad)
            joyStick.aileron = (joyStick.aileron * strength) / 100
            joyStick.elevator = (joyStick.elevator * strength) / 100
            sendValuesToServer()
        }

    }
    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }
    private fun sendValuesToServer() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(url)
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
                        if(!response.message().equals("OK")){
                            Toast.makeText(cont, response.message(), Toast.LENGTH_SHORT).show()
                        }
                }
            })
    }

    public fun getScreenshotFromServer() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if(response.message().equals("OK")) {

                    val I = response.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    runOnUiThread {
                        SimulatorView.setImageBitmap(B)
                    }
                } else{
                    Toast.makeText(cont, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(cont, "Connection failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun convertFromUrlToIpAndPort(){
        var i = url.length -1
        var portS = ""
        while(url[i] != ':') {
            portS = url[i] + portS
            i--
        }
        port = portS.toInt()
        i--
        while(url[i] != '/'){
            ip = url[i] + ip
            i--
        }
    }
}