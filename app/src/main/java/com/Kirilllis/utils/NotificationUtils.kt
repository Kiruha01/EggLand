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
import androidx.core.app.NotificationCompat
import com.Kirilllis.MainActivity
import com.Kirilllis.R
import com.Kirilllis.receivers.NotifyReceiver
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtils {
    companion object{
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Timer App"
        private const val TIMER_ID = 0

        const val ACTION_START = "start"
        const val ACTION_RESUME = "resume"
        const val ACTION_STOP = "stop"
        const val ACTION_PAUSE = "pause"

        fun showTimerExpired(id: Int, context: Context){
            val startIntent = Intent(context, NotifyReceiver::class.java)
            startIntent.action = ACTION_START
            startIntent.putExtra("id", id)
            val startPendingIntent = PendingIntent.getBroadcast(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val nBuilder = getNotiBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(MainActivity.listOfTimers[id].name)
                .setContentText("Start Again?")
                // TODO.setSmallIcon(R.drawable.ic_icon)
                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                // TODO.addAction(R.drawable.ic_play_arrow, "Start", startPendingIntent)
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(true, CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER)
            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun showTimerPaused(id: Int, context: Context){
            val startIntent = Intent(context, NotifyReceiver::class.java)
            startIntent.action = ACTION_RESUME
            startIntent.putExtra("id", id)
            val startPendingIntent = PendingIntent.getBroadcast(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val nBuilder = getNotiBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(MainActivity.listOfTimers[id].name)
                .setContentText("Resume?")
                // TODO .setSmallIcon(R.drawable.ic_icon)
                .setOngoing(true)
                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
            // TODO .addAction(R.drawable.ic_play_arrow, "Resume", startPendingIntent)
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(true, CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER)
            nManager.notify(TIMER_ID, nBuilder.build())
        }

        fun showTimerRunning(id: Int, context: Context, wakeuptime: Long){
            val pauseIntent = Intent(context, NotifyReceiver::class.java)
            pauseIntent.action = ACTION_PAUSE
            pauseIntent.putExtra("id", id)
            val pausePendingIntent = PendingIntent.getBroadcast(context, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val stopIntent = Intent(context, NotifyReceiver::class.java)
            stopIntent.action = ACTION_STOP
            stopIntent.putExtra("id", id)
            val stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)


            val nBuilder = getNotiBuilder(context, CHANNEL_ID_TIMER, true)
            nBuilder.setContentTitle(MainActivity.listOfTimers[id].name)
                .setContentText("End: ${df.format(Date(wakeuptime))}")
                .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                .setOngoing(true)
                // TODO .setSmallIcon(R.drawable.ic_icon)
                // TODO.addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
                // TODO.addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(true, CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER)
            nManager.notify(TIMER_ID, nBuilder.build())
        }

        private fun getNotiBuilder(
            context: Context,
            channelId: String,
            playSound: Boolean
        ): NotificationCompat.Builder {
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

            val stackBuiler = TaskStackBuilder.create(context)
            stackBuiler.addParentStack(javaClass)
            stackBuiler.addNextIntent(resultIntent)

            return stackBuiler.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        private fun NotificationManager.createNotificationChannel(playSound: Boolean, channelId: String, channelName: String){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                else NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelId, channelName, channelImportance)
                this.createNotificationChannel(nChannel)
            }
        }
        fun hideNot(context: Context){
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }
    }
}