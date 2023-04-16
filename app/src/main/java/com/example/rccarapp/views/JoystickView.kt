package com.example.rccarapp.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import okhttp3.*
import java.io.IOException
import kotlin.math.*

class JoystickView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
    }

    private val stickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
    }

    private val centerPoint = PointF()

    private val stickRadius: Float
        get() = min(width, height) * 0.3f

    private val stickPosition = PointF()

    private var showToast = false

    private var listener: ((Float, Float) -> Unit)? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerPoint.x = w /2f
        centerPoint.y = h / 2f
    }

    override fun onDraw(canvas: Canvas) {
        // draw background circle
        canvas.drawCircle(centerPoint.x, centerPoint.y, stickRadius, bgPaint)

        // draw stick circle
        canvas.drawCircle(stickPosition.x, stickPosition.y, stickRadius * 0.5f, stickPaint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val distance = sqrt(
                    (event.x - centerPoint.x).pow(2) + (event.y - centerPoint.y).pow(2)
                )
                if (distance > stickRadius) {
                    // limit the stick position to the edge of the background circle
                    val angle = atan2(event.y - centerPoint.y, event.x - centerPoint.x)
                    stickPosition.x = centerPoint.x + (stickRadius * kotlin.math.cos(angle))
                    stickPosition.y = centerPoint.y + (stickRadius * kotlin.math.sin(angle))
                } else {
                    stickPosition.x = event.x
                    stickPosition.y = event.y
                }
                listener?.invoke(
                    (stickPosition.x - centerPoint.x) / stickRadius,
                    (stickPosition.y - centerPoint.y) / stickRadius
                )
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                showToast = false
                stickPosition.x = centerPoint.x
                stickPosition.y = centerPoint.y
                // listener?.invoke(0f, 0f)
                invalidate()
            }
        }
        return true
    }

    fun setJoystickListener(listener: (Float, Float) -> Unit) {
        this.listener = { x, y ->

            val http = OkHttpClient()
            val direction = getDirection(x, y)
            // Toast.makeText(context, direction.toString(), Toast.LENGTH_SHORT).show()
            val message = when (direction) {
                Direction.LEFT -> "Joystick moved to the left"
                Direction.RIGHT -> "fb moved to the right"
                Direction.TOP -> "Joystick moved to the top"
                Direction.BOTTOM -> "Joystick moved to the bottom"
                else -> "Joystick moved in an unknown direction"
            }
            if (!showToast) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                if(message.equals("Joystick moved to the left")){
//                    val request = Request.Builder().url("http://192.168.255.42:5000/left").build()
//                    http.newCall(request).enqueue(object : Callback {
//                        override fun onFailure(call: Call, e: IOException) {}
//                        override fun onResponse(call: Call, response: Response) {} })


                }else if (message.equals("fb moved to the right")){
//                    val request = Request.Builder().url("http://192.168.255.42:5000/right").build()
//                    http.newCall(request).enqueue(object : Callback {
//                        override fun onFailure(call: Call, e: IOException) {}
//                        override fun onResponse(call: Call, response: Response) {} })


                }else if(message.equals("Joystick moved to the top")){
//                    val request = Request.Builder().url("http://192.168.255.42:5000/forward").build()
//                    http.newCall(request).enqueue(object : Callback {
//                        override fun onFailure(call: Call, e: IOException) {
//                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
//                        }
//                        override fun onResponse(call: Call, response: Response) {}
//                    })
                }


                else if(message.equals("Joystick moved to the bottom")){
//                    val request = Request.Builder().url("http://192.168.255.42:5000/backward").build()
//                    http.newCall(request).enqueue(object : Callback {
//                        override fun onFailure(call: Call, e: IOException) {}
//                        override fun onResponse(call: Call, response: Response) {} })
                }
                showToast = true
            }

        }
    }

    private fun getDirection(x: Float, y: Float): Direction {

        //val angle = atan2(y - centerPoint.y + height / 4, x - centerPoint.x + width / 4)
        val angle = Math.toDegrees(Math.atan2(y.toDouble(), x.toDouble())).toFloat()


        //  Toast.makeText(context, angle.toString(), Toast.LENGTH_SHORT).show()
        val adjustedAngle = angle + 45
        // Adjust angle to center at 0 degrees

   //   Toast.makeText(context, (adjustedAngle%360).toString(), Toast.LENGTH_SHORT).show()


        return when (adjustedAngle%360) {


            in -80.0..0.0 -> Direction.TOP
            in 0.0..95.0 -> Direction.RIGHT
            in 95.0..200.0 -> Direction.BOTTOM
            else -> Direction.LEFT
        }

    }

    enum class Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }


}

