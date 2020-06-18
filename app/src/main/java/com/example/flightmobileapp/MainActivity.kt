package com.example.flightmobileapp

import Api
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    //var url_table: UrlDatabase = getInstance(applicationContext);
    var localHosts = arrayOfNulls<UrlData>(5)
    var db = DataBaseHandler(this)
    var bol: Boolean = false

    /*var image :ImageView = ImageView(this)*/
    var lst: MutableList<LocalHostAddress> = ArrayList()
    var counter: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lst = db.readData()
        counter = lst.size
        fromListToButtons()

        TextBox.setOnClickListener() {
            TextBox.text.clear()
        }
        localH1.setOnClickListener() {
            TextBox.setText(localH1.text.toString())
        }
        localH2.setOnClickListener() {
            TextBox.setText(localH2.text.toString())
        }
        localH3.setOnClickListener() {
            TextBox.setText(localH3.text.toString())
        }
        localH4.setOnClickListener() {
            TextBox.setText(localH4.text.toString())
        }
        localH5.setOnClickListener() {
            TextBox.setText(localH5.text.toString())
        }

        val connect = findViewById<Button>(R.id.connect)
        connect.setOnClickListener {
            var pattern =
                Regex("http?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")
            var result = pattern.containsMatchIn(TextBox.text.toString())
            if (!result)
                return@setOnClickListener
            checkAndGetScreenshotFromServer()
            sleep(20000)
            if (!bol)
                return@setOnClickListener
            if (TextBox.text.toString().length > 0) {
                val localHostAddress =
                    LocalHostAddress(TextBox.text.toString(), System.currentTimeMillis())
                db.insertData(localHostAddress)
                lst = db.readData()
                var x = 1
            }
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("url", TextBox.text.toString())
            //intent.putExtra("simulatorScreen", image)
            startActivity(intent)
        }
    }

    private fun fromListToButtons() {
        if (counter >= 1) {
            localH1.text = lst[0].address
        }
        if (counter >= 2) {
            localH2.text = lst[1].address
        }
        if (counter >= 3) {
            localH3.text = lst[2].address
        }
        if (counter >= 4) {
            localH4.text = lst[3].address
        }
        if (counter >= 5) {
            localH5.text = lst[4].address
        }
    }

    public fun checkAndGetScreenshotFromServer() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(TextBox.text.toString())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                bol = true
                val I = response.body()?.byteStream()
                val B = BitmapFactory.decodeStream(I)
                image1.setImageBitmap(B)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                print("basa")
            }
        })
        var check = 1
    }
}