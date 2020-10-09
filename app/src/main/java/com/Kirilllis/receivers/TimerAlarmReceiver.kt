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
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        NotificationUtils.showTimerExpired(intent.getIntExtra("id", 0), context)
        PrefUtils.setTimerState(intent.getIntExtra("id", 0), TimerTile.TimerState.Stopped, context)
        AlarmUtils.removeAlarm(intent.getIntExtra("id", 0) , context)
        val int = Intent(context, TimeIsUp::class.java)
        int.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(int)
    }
}
