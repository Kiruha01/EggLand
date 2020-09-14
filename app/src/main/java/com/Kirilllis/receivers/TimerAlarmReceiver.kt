package com.Kirilllis.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.Kirilllis.TimerTile
import com.Kirilllis.utils.AlarmUtils
import com.Kirilllis.utils.NotificationUtils
import com.Kirilllis.utils.PrefUtils

class TimerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("DEBUG", "received")
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        NotificationUtils.showTimerExpired(intent.getIntExtra("id", 0), context)
        PrefUtils.setTimerState(intent.getIntExtra("id", 0), TimerTile.TimerState.Stopped, context)
        AlarmUtils.removeAlarm(intent.getIntExtra("id", 0) , context)
    }
}
