package com.example.flightmobileapp
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
//import android.support.v8.1.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.shapes.OvalShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
/*        val bitmap: Bitmap = Bitmap.createBitmap(700, 1000, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(bitmap)
        var left = 200
        var top = 400
        var right = 600
        var bottom = 800
        var shapeDrawable:ShapeDrawable
        shapeDrawable = ShapeDrawable(OvalShape())
        shapeDrawable.setBounds(left,top,right,bottom)
        shapeDrawable.getPaint().setColor(Color.parseColor("#009191"))
        shapeDrawable.draw(canvas)*/

    }
}