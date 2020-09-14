package com.Kirilllis.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.ListAdapter
import androidx.core.app.NotificationCompat
import com.Kirilllis.MainActivity
import com.Kirilllis.R
import com.Kirilllis.TimerTile
import com.Kirilllis.receivers.NotifyReceiver
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

/*
* Класс для работы с уведомлениями
* */

class NotificationUtils {
    companion object{
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Timer App"
        private const val TIMER_ID = 0

        const val ACTION_START = "start"
        const val ACTION_RESUME = "resume"
        const val ACTION_STOP = "stop"
        const val ACTION_PAUSE = "pause"

        // Время вышло
        fun showTimerExpired(id: Int, context: Context){
            val startIntent = Intent(context, NotifyReceiver::class.java)
            startIntent.action = ACTION_START
            startIntent.putExtra("id", id)
            val startPendingIntent = PendingIntent.getBroadcast(context, id, startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val nBuilder = getNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(MainActivity.listOfTimers[id].name)
                .setContentText("Start Again?")
                .setSmallIcon(R.drawable.ic_timer_finish)
                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                .addAction(R.drawable.ic_play_arrow, "Start", startPendingIntent)
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(true, CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER)
            nManager.notify(id, nBuilder.build())
        }

        // На паузе
        fun showTimerPaused(id: Int, context: Context){
            val startIntent = Intent(context, NotifyReceiver::class.java)
            startIntent.action = ACTION_RESUME
            startIntent.putExtra("id", id)
            val startPendingIntent = PendingIntent.getBroadcast(context, id, startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val nBuilder = getNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(MainActivity.listOfTimers[id].name)
                .setContentText("Resume?")
                .setSmallIcon(R.drawable.ic_timer)
                .setOngoing(true)
                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                .addAction(R.drawable.ic_play_arrow, "Resume", startPendingIntent)
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(true, CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER)
            nManager.notify(id, nBuilder.build())
        }

        // Во время работы
        fun showTimerRunning(id: Int, context: Context, wakeuptime: Long){
            val pauseIntent = Intent(context, NotifyReceiver::class.java)
            pauseIntent.action = ACTION_PAUSE
            pauseIntent.putExtra("id", id)
            val pausePendingIntent = PendingIntent.getBroadcast(context, id, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val stopIntent = Intent(context, NotifyReceiver::class.java)
            stopIntent.action = ACTION_STOP
            stopIntent.putExtra("id", id)
            val stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)


            val nBuilder = getNotificationBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(MainActivity.listOfTimers[id].name)
                .setContentText("End: ${df.format(Date(wakeuptime))}")
                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                .setOngoing(true)
                .setVibrate(longArrayOf(100L,500L,100L))
                .setSmallIcon(R.drawable.ic_timer)
                .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
                .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(true, CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER)
            nManager.notify(id, nBuilder.build())
        }

        private fun getNotificationBuilder(context: Context, channelId: String, playSound: Boolean): NotificationCompat.Builder {
            val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val nBuilder = NotificationCompat.Builder(context, channelId)
                .setAutoCancel(true)
                .setDefaults(0)
            if (playSound){
                nBuilder.setSound(notificationSound)
            }
            return nBuilder
        }

        private fun <T> getPendingIntentWithStack(context: Context, javaClass: Class<T>) : PendingIntent {
            val resultIntent = Intent(context, javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or  Intent.FLAG_ACTIVITY_SINGLE_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        private fun NotificationManager.createNotificationChannel(playSound: Boolean, channelId: String, channelName: String){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelId, channelName, channelImportance)
                nChannel.enableVibration(true)
                this.createNotificationChannel(nChannel)
            }
        }
        fun hideNotifications(id: Int, context: Context){
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            for (i in 0..id)
                nManager.cancel(i)
        }

        private fun findNearestTimerWithState(timers: Array<TimerTile>, state: TimerTile.TimerState): Int{
            var idMinSeconds = -1
            for (timerId in timers.indices){
                if (timers[timerId].state == TimerTile.TimerState.Running && idMinSeconds == -1)
                    idMinSeconds = timerId
                else if (timers[timerId].state == TimerTile.TimerState.Running &&
                    timers[timerId].getRemainingSeconds() < timers[idMinSeconds].getRemainingSeconds()){
                    idMinSeconds = timerId
                }
            }
            return idMinSeconds
        }

        fun showNearestNotification(timers: Array<TimerTile>, context: Context){
//            var idMinSeconds = findNearestTimerWithState(timers, TimerTile.TimerState.Running)
//            if (idMinSeconds == -1)
//                idMinSeconds = findNearestTimerWithState(timers, TimerTile.TimerState.Paused)
//            if (idMinSeconds > -1){
//                val thisTimer = timers[idMinSeconds]
            for(i in timers.indices){
                when(timers[i].state){
                    TimerTile.TimerState.Running -> showTimerRunning(timers[i].id, context,
                        AlarmUtils.nowSeconds*1000 + timers[i].getRemainingSeconds()*1000)
                    TimerTile.TimerState.Paused -> showTimerPaused(timers[i].id, context)
                }
            }
        }
    }
}