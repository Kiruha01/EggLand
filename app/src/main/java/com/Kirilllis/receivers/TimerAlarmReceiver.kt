package com.Kirilllis.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.Kirilllis.TimeIsUp
import com.Kirilllis.TimerTile
import com.Kirilllis.utils.AlarmUtils
import com.Kirilllis.utils.NotificationUtils
import com.Kirilllis.utils.PrefUtils

class TimerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("DEBUG", "received")
        val id = intent.getIntExtra("id", 0)
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        NotificationUtils.showTimerExpired(id, context)
        PrefUtils.setTimerState(id, TimerTile.TimerState.Stopped, context)
        AlarmUtils.removeAlarm(id , context)
        val int = Intent(context, TimeIsUp::class.java)
            .putExtra("id", id)
        int.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(int)
    }
}
