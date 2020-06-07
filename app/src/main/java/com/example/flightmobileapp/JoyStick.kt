package com.example.flightmobileapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class Joystick {
    val STICK_NONE = 0
    val STICK_UP = 1
    val STICK_UPRIGHT = 2
    val STICK_RIGHT = 3
    val STICK_DOWNRIGHT = 4
    val STICK_DOWN = 5
    val STICK_DOWNLEFT = 6
    val STICK_LEFT = 7
    val STICK_UPLEFT = 8

    private var mContext : Context? = null
    private var mLayout : ViewGroup? = null
    private var params : ViewGroup.LayoutParams? = null
    private var draw : DrawCanvas? = null
    private var paint : Paint? = null
    private var stick : Bitmap? = null

    private var touch_state = false

    private var STICK_ALPHA = 200
    private var LAYOUT_ALPHA = 200
    private var OFFSET = 0

    private var stick_width = 0
    private var stick_height = 0
    private var position_x = 0
    private var position_y = 0
    private var min_distance = 0
    private var distance = 0f
    private var angle = 0f

    constructor( context: Context?, layout: ViewGroup?, stick_res_id: Int){
        mContext = context
        stick = BitmapFactory.decodeResource(mContext!!.resources, stick_res_id)
        stick_width = stick!!.getWidth()
        stick_height = stick!!.getHeight()
        draw = DrawCanvas(mContext)
        paint = Paint()
        mLayout = layout
        params = mLayout!!.layoutParams
    }

    fun getStickAlpha(): Int {
        return STICK_ALPHA
    }

    fun setStickAlpha(alpha: Int) {
        STICK_ALPHA = alpha
        paint!!.alpha = alpha
    }

    fun getLayoutAlpha(): Int {
        return LAYOUT_ALPHA
    }

    fun setLayoutAlpha(alpha: Int) {
        LAYOUT_ALPHA = alpha
        mLayout!!.background.alpha = alpha
    }

    fun getOffset(): Int {
        return OFFSET
    }

    fun setOffset(offset: Int) {
        OFFSET = offset
    }

    fun getStickWidth(): Int {
        return stick_width
    }

    fun setStickWidth(width: Int) {
        stick = Bitmap.createScaledBitmap(stick!!, width, stick_height, false)
        stick_width = stick!!.getWidth()
    }

    fun getStickHeight(): Int {
        return stick_height
    }

    fun setStickHeight(height: Int) {
        stick = Bitmap.createScaledBitmap(stick!!, stick_width, height, false)
        stick_height = stick!!.getHeight()
    }

    fun getX(): Int {
        if (distance > min_distance && touch_state) {
            return position_x
        } else {
            return 0
        }
    }

    fun getY(): Int {
        if (distance > min_distance && touch_state) {
            return position_y
        } else {
            return 0
        }
    }

    fun getMinimumDistance(): Int {
        return min_distance
    }

    fun setMinimumDistance(minDistance: Int) {
        min_distance = minDistance
    }

    fun getDistance(): Float {
        if (distance > min_distance && touch_state) {
            return distance
        } else {
            return 0F
        }
    }

    fun getAngle(): Float {
        if (distance > min_distance && touch_state) {
            return angle
        } else {
            return 0F
        }
    }

    fun setStickSize(width: Int, height: Int) {
        stick = Bitmap.createScaledBitmap(stick!!, width, height, false)
        stick_width = stick!!.getWidth()
        stick_height = stick!!.getHeight()
    }

    fun setLayoutSize(width: Int, height: Int) {
        params!!.width = width
        params!!.height = height
    }

    fun getLayoutWidth(): Int {
        return params!!.width
    }

    fun getLayoutHeight(): Int {
        return params!!.height
    }

    fun getPosition(): IntArray? {
        if (distance > min_distance && touch_state) {
            return intArrayOf(position_x, position_y)
        } else {
            return intArrayOf(0, 0)
        }
    }

    fun drawStick(arg1: MotionEvent) {
        position_x = (arg1.x - params!!.width / 2).toInt()
        position_y = (arg1.y - params!!.height / 2).toInt()
        distance = Math.sqrt(Math.pow(position_x.toDouble(),2.0) + Math.pow(position_y.toDouble(), 2.0)).toFloat()
        angle = calculateAngle(position_x.toFloat(), position_y.toFloat()).toFloat()
        if (arg1.action == MotionEvent.ACTION_DOWN) {
            if (distance <= params!!.width / 2 - OFFSET) { draw!!.position(arg1.x.toDouble(), arg1.y.toDouble())
                draw()
                touch_state = true
            }
        } else if (arg1.action == MotionEvent.ACTION_MOVE && touch_state) {
            if (distance <= params!!.width / 2 - OFFSET) {
                draw!!.position(arg1.x.toDouble(), arg1.y.toDouble())
                draw()
            } else if (distance > params!!.width / 2 - OFFSET) {
                var x = (Math.cos(Math.toRadians(calculateAngle(position_x.toFloat(), position_y.toFloat()))) * (params!!.width / 2 - OFFSET)).toFloat()
                var y = (Math.sin(Math.toRadians(calculateAngle(position_x.toFloat(), position_y.toFloat()))) * (params!!.height / 2 - OFFSET)).toFloat()
                x += (params!!.width / 2).toFloat()
                y += (params!!.height / 2).toFloat()
                draw!!.position(x.toDouble(), y.toDouble())
                draw()
            } else {
                mLayout!!.removeView(draw)
            }
        } else if (arg1.action == MotionEvent.ACTION_UP) {
            mLayout!!.removeView(draw)
            touch_state = false
        }
    }

    fun get8Direction(): Int {
        if (distance > min_distance && touch_state) {
            if (angle >= 247.5 && angle < 292.5) {
                return STICK_UP
            } else if (angle >= 292.5 && angle < 337.5) {
                return STICK_UPRIGHT
            } else if (angle >= 337.5 || angle < 22.5) {
                return STICK_RIGHT
            } else if (angle >= 22.5 && angle < 67.5) {
                return STICK_DOWNRIGHT
            } else if (angle >= 67.5 && angle < 112.5) {
                return STICK_DOWN
            } else if (angle >= 112.5 && angle < 157.5) {
                return STICK_DOWNLEFT
            } else if (angle >= 157.5 && angle < 202.5) {
                return STICK_LEFT
            } else if (angle >= 202.5 && angle < 247.5) {
                return STICK_UPLEFT
            }
        } else if (distance <= min_distance && touch_state) {
            return STICK_NONE
        }
        return 0
    }

    fun get4Direction(): Int {
        if (distance > min_distance && touch_state) {
            if (angle >= 45 && angle < 135) {
                return STICK_UP
            } else if (angle <= 45 && angle > 315) {
                return STICK_RIGHT
            } else if (angle >= 215 && angle < 315) {
                return STICK_DOWN
            } else if (angle >= 135 && angle < 225) {
                return STICK_LEFT
            }
        } else if (distance <= min_distance && touch_state) {
            return STICK_NONE
        }
        return 0
    }

    fun get2Direction() : Int {
        if(distance > min_distance && touch_state){
            if (angle <= 90 && angle >= 270 ){
                return STICK_RIGHT
            } else if (angle >= 90 && angle <= 270){
                return STICK_LEFT
            }
        } else if (distance <= min_distance && touch_state){
            return STICK_NONE
        }
        return 0
    }

    private fun calculateAngle(x: Float, y: Float): Double {
        if (x >= 0 && y >= 0) {
            return Math.toDegrees(Math.atan(y / x.toDouble()))
        } else if (x < 0 && y >= 0) {
            return Math.toDegrees(Math.atan(y / x.toDouble())) + 180
        } else if (x < 0 && y < 0) {
            return Math.toDegrees(Math.atan(y / x.toDouble())) + 180
        } else if (x >= 0 && y < 0){
            return Math.toDegrees(Math.atan(y / x.toDouble())) + 360
        }
        return 0.0
    }

    private fun draw() {
        try {
            mLayout!!.removeView(draw)
        } catch (e: Exception) {
        }
        mLayout!!.addView(draw)
    }

    inner class DrawCanvas constructor(mContext: Context?) :
        View(mContext) {
        var x : Double = 0.0
        var y : Double = 0.0

        public override fun onDraw(canvas: Canvas) {
            canvas.drawBitmap(stick!!, x.toFloat(), y.toFloat(), paint)
        }

        fun position(pos_x: Double, pos_y: Double) {
            x = pos_x - stick_width / 2
            y = pos_y - stick_height / 2
        }
    }

}
