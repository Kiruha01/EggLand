package com.Kirilllis.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.Kirilllis.TimerTile
import com.Kirilllis.utils.AlarmUtils
import com.Kirilllis.utils.NotificationUtils
import com.Kirilllis.utils.PrefUtils

/*
* Приёмник событий с кнопок уведомлений
*/

class NotifyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("id", 0)
        when(intent.action){
            NotificationUtils.ACTION_STOP -> {
                AlarmUtils.removeAlarm(id, context)
                PrefUtils.setTimerState(id, TimerTile.TimerState.Stopped, context)
                NotificationUtils.hideNotifications(id, context)
            }
            NotificationUtils.ACTION_PAUSE-> {
                var secondsRemaining = PrefUtils.getSecondsRemaining(id, context)
                val setAlarmTimer = PrefUtils.getAlarmSetTime(id, context)
                val nowSeconds = AlarmUtils.nowSeconds
                secondsRemaining -= nowSeconds - setAlarmTimer
                PrefUtils.setSecondsRemaining(id, secondsRemaining, context)

                AlarmUtils.removeAlarm(id, context)
                PrefUtils.setTimerState(id, TimerTile.TimerState.Paused, context)
                NotificationUtils.showTimerPaused(id, context)
            }
            NotificationUtils.ACTION_RESUME -> {
                val secondsRemaining = PrefUtils.getSecondsRemaining(id, context)
                val wakeUpTime = AlarmUtils.setAlarm(id, context, AlarmUtils.nowSeconds, secondsRemaining)
                PrefUtils.setTimerState(id, TimerTile.TimerState.Running, context)
                NotificationUtils.showTimerRunning(id, context, wakeUpTime)
            }
            NotificationUtils.ACTION_START -> {
                val secRemaining = PrefUtils.getTimerLength(id, context)
                val wakeUpTime = AlarmUtils.setAlarm(id, context, AlarmUtils.nowSeconds, secRemaining)
                PrefUtils.setTimerState(id, TimerTile.TimerState.Running, context)
                PrefUtils.setSecondsRemaining(id, secRemaining, context)
                NotificationUtils.showTimerRunning(id, context, wakeUpTime)
            }
        }
    }
}
