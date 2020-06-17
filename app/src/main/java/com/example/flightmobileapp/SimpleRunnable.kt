package com.example.flightmobileapp

import Api
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_game.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SimpleRunnable : Runnable {
     fun run(SimulatorView: ImageView) {
        while (true) {
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
                    SimulatorView.setImageBitmap(B)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    print("problema")
                }
            })
        }
    }

    override fun run() {
        var x = 1
    }
}