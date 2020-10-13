package com.Kirilllis

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_time_is_up.*

class TimeIsUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG", "onCreate() - -- -")
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_time_is_up)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        val id = intent.getIntExtra("id", 0)
        val tArray = MainActivity.context.resources.obtainTypedArray(R.array.images)
        IM_food.setImageResource(tArray.getResourceId(id, R.drawable.ooops))

        Log.d("DEBUG", "onCreate() - -- +")
    }

    override fun onStart() {
        super.onStart()
        Log.d("DEBUG", "onStart() - -- -")
    }

    override fun onPause() {
        super.onPause()
    }
}