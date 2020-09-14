package com.Kirilllis.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.Kirilllis.receivers.TimerAlarmReceiver
import java.util.*
/*
* Утилиты для запуска фонового таймера
* */
class AlarmUtils {
    companion object {
        fun setAlarm(id: Int, context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining)*1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerAlarmReceiver::class.java)
                .putExtra("id", id)
            val pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtils.setAlarmSetTime(id, nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(id: Int, context: Context){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerAlarmReceiver::class.java)
                .putExtra("id", id)
            val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
            alarmManager.cancel(pendingIntent)
            PrefUtils.setAlarmSetTime(id, 0, context)
        }

        // Текущее время
        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000

    }
}