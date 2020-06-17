package com.example.flightmobileapp.Models

import com.google.gson.annotations.SerializedName

class JoyStickData(ail:Double,rud:Double,thr:Double,ele:Double) {
    @SerializedName("aileron") var aileron: Double = ail
    @SerializedName("rudder") var rudder: Double = rud
    @SerializedName("throttle") var throttle: Double = thr
    @SerializedName("elevator")var elevator: Double = ele
}