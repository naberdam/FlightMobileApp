package com.example.flightmobileapp

import Api
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.flightmobileapp.GameActivity.Companion.B1
import com.example.flightmobileapp.GameActivity.Companion.convertResponseToStatusMessage
import com.example.flightmobileapp.Models.JoyStickData
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    var db = DataBaseHandler(this)
    var cont: Context = this
    var lst: MutableList<LocalHostAddress> = ArrayList()
    var counter: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lst = db.readData()
        counter = lst.size
        fromListToButtons()

        TextBox.setOnClickListener {
            TextBox.text.clear()
        }
        localH1.setOnClickListener {
            TextBox.setText(localH1.text.toString())
        }
        localH2.setOnClickListener {
            TextBox.setText(localH2.text.toString())
        }
        localH3.setOnClickListener {
            TextBox.setText(localH3.text.toString())
        }
        localH4.setOnClickListener {
            TextBox.setText(localH4.text.toString())
        }
        localH5.setOnClickListener {
            TextBox.setText(localH5.text.toString())
        }
        val connect = findViewById<Button>(R.id.connect)
        connect.setOnClickListener {
            saveData()
            TextBox.setText(removeLastSlash(TextBox.text.toString()))
            val pattern1 =
                Regex("http?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")
            val pattern2 =
                Regex("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%.\\+~#=]{2,256}\\:[0-9]{4,8}\\b([-a-zA-Z0-9@:%\\+.~#?&//=]*)")
            val result = pattern1.containsMatchIn(TextBox.text.toString())
            val result2 = pattern2.containsMatchIn(TextBox.text.toString())
            if (!result && !result2) {
                Toast.makeText(cont, "invalid URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            checkAndGetScreenshotFromServer()
/*            if (TextBox.text.toString().length > 0) {
                val localHostAddress =
                    LocalHostAddress(TextBox.text.toString(), System.currentTimeMillis())
                db.insertData(localHostAddress)
                lst = db.readData()
                var x = 1
            }
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("url", TextBox.text.toString())
            //intent.putExtra("simulatorScreen", image)
            startActivity(intent)*/
        }
    }

    private fun fromListToButtons() {
        if (lst.size >= 1) {
            localH1.text = lst[0].address
        }
        if (lst.size >= 2) {
            localH2.text = lst[1].address
        }
        if (lst.size >= 3) {
            localH3.text = lst[2].address
        }
        if (lst.size >= 4) {
            localH4.text = lst[3].address
        }
        if (lst.size >= 5) {
            localH5.text = lst[4].address
        }
    }

    fun checkAndGetScreenshotFromServer() {
        Toast.makeText(cont, "Trying to connect..", Toast.LENGTH_LONG).show()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(TextBox.text.toString())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        //Toast.makeText(cont, "drink some coffee in the meantime", Toast.LENGTH_SHORT).show()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(cont, "Succeed", Toast.LENGTH_SHORT).show()
                    val I = response.body()?.byteStream()
                    B1 = BitmapFactory.decodeStream(I)
                    saveDataAndSwitchToNextActivity()
                } else {
                    Toast.makeText(
                        cont,
                        convertResponseToStatusMessage(response),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(cont, "connection failed", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun saveDataAndSwitchToNextActivity() {
        val localHostAddress =
            LocalHostAddress(TextBox.text.toString(), System.currentTimeMillis())
        db.insertData(localHostAddress)
        lst = db.readData()
        val intent = Intent(this, GameActivity::class.java)
        var url = removeLastSlash(TextBox.text.toString())
        intent.putExtra("url", TextBox.text.toString())
        startActivity(intent)
    }

    private fun saveData() {
        val localHostAddress =
            LocalHostAddress(TextBox.text.toString(), System.currentTimeMillis())
        db.insertData(localHostAddress)
        lst = db.readData()
        fromListToButtons()
    }

    private fun removeLastSlash(url: String): String {
        if (url[url.length - 1] == '/')
            return url.substring(0, url.length - 1)
        else
            return url
    }
    //when app pause
    override fun onPause() {
        super.onPause()
    }

    //when app destroy
    override fun onDestroy() {
        super.onDestroy()
    }

    //when app start
    override fun onStart() {
        super.onStart()
    }

    //when app stop
    override fun onStop() {
        super.onStop()
    }

    //when app resume
    override fun onResume() {
        super.onResume()
    }
}