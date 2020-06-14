package com.example.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val db = LocalHostDataBase.getDatabase(applicationContext)
//        val localHosts = db.localHostDao()
//        val gson = GsonBuilder()
//            .setLenient()
//            .create()
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8686/")
//            .addConverterFactory(GsonConverterFactory.create(gson))
        setContentView(R.layout.activity_main)

        val url = findViewById<Button>(R.id.URL)
        url.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        val connect = findViewById<Button>(R.id.connect)
        connect.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}