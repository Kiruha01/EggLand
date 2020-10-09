package com.Kirilllis

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager

class TimeIsUp : AppCompatActivity() {
    var wl: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG", "onCreate() - -- -")
        super.onCreate(savedInstanceState)
        val pm = this.getSystemService(Context.POWER_SERVICE) as PowerManager
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,"TestAlarm:tag")
        wl?.acquire(10*60*1000L/*10 minutes*/)

        setContentView(R.layout.activity_time_is_up)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
//            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//            keyguardManager.requestDismissKeyguard(this, null)
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

        Log.d("DEBUG", "onCreate() - -- +")
    }

    override fun onStart() {
        super.onStart()
        Log.d("DEBUG", "onStart() - -- -")
    }

    override fun onPause() {
        super.onPause()
        wl?.release()
    }
}