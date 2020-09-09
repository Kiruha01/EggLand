package com.Kirilllis.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.Kirilllis.receivers.TimerReceiver
import java.util.*

class AlarmUtils {
    companion object {
        fun setAlarm(context: Context, id: Int, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining)*1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerReceiver::class.java)
                .putExtra("id", id)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtils.setAlarmSetTime(id, nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context, id: Int){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerReceiver::class.java)
                .putExtra("id", id)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.cancel(pendingIntent)
            PrefUtils.setAlarmSetTime(id, 0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000

    }
}