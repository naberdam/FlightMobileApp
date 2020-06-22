package com.example.flightmobileapp
//import android.support.v8.1.app.AppCompatActivity


//import android.support.v7.app.AppCompatActivity
import Api
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flightmobileapp.Models.JoyStickData
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_game.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Math.toRadians


class GameActivity/*(var url: String)*/ : AppCompatActivity() {
    //handler that handle the screenshot image every 700ms
    private lateinit var mainHandler: Handler
    private lateinit var url: String
    private var ip: String = ""
    private var countNumOfFails: Int = 0
    private var port: Int = 0
    private var cont: Context = this
    private var flagForPause:Boolean = false
    private val updateTextTask = object : Runnable {
        override fun run() {
            if(!flagForPause)
                getScreenshotFromServer()
            mainHandler.postDelayed(this, 450)
        }
    }

    //private val client = CommandClient(ip, port)
    private var joyStick = JoyStickData(0.0, 0.0, 0.0, 0.0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra("url") != null) {
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

    override fun onPause() {
        super.onPause()
        flagForPause = true
    }

    override fun onDestroy() {
        super.onDestroy()
        flagForPause = true
    }

    override fun onStart() {
        super.onStart()
        flagForPause = false
    }

    override fun onStop() {
        super.onStop()
        flagForPause = true
    }

    override fun onResume() {
        super.onResume()
        flagForPause = false
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
                    countNumOfFails++
                    if (countNumOfFails > 15) {
                        Toast.makeText(
                            applicationContext, "Connection failed",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (!response.message().equals("OK")) {
                        countNumOfFails++
                        if (countNumOfFails > 15)
                            Toast.makeText(cont, convertResponseToStatusMessage(response), Toast.LENGTH_SHORT).show()
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
                if (response.message().equals("OK")) {

                    val I = response.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    countNumOfFails = 0
                    runOnUiThread {
                        SimulatorView.setImageBitmap(B)
                    }
                } else {
                    countNumOfFails++
                    if (countNumOfFails > 13)
                        Toast.makeText(
                            cont,
                            convertResponseToStatusMessage(response),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(cont, "Connection failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun convertFromUrlToIpAndPort() {
        var i = url.length - 1
        var portS = ""
        while (url[i] != ':') {
            portS = url[i] + portS
            i--
        }
        port = portS.toInt()
        i--
        while (url[i] != '/') {
            ip = url[i] + ip
            i--
        }
    }
    companion object {
        public fun convertResponseToStatusMessage(response: Response<ResponseBody>): String {
            var reader: BufferedReader? = null
            val sb = StringBuilder()
            try {
                reader =
                    BufferedReader(InputStreamReader(response.errorBody()!!.byteStream()))
                var line: String?
                try {
                    while (reader.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val finallyError = sb.toString()
            return finallyError
        }
    }
}