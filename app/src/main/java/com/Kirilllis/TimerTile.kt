package com.Kirilllis

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import com.Kirilllis.utils.AlarmUtils
import com.Kirilllis.utils.NotificationUtils
import com.Kirilllis.utils.PrefUtils
import kotlinx.android.synthetic.main.timer_item.view.*
/*
* Класс, который хранит все данные о каждом таймере.
* params:
*   id - идентификатор таймера для доступа к остальным возможностям
*   name - имя таймера
*   idPicture - идентификатор иконки таймера
*   context - контекст приложения
*
* state - состояние таймера
* secondsRemaining - оставшееся количество секунд
* timer - сам таймер, который отсчитывает секунды
*
* */

class TimerTile(val id: Int, val name: String, var lengthInSeconds: Long, val idPicture: Int, val context: Context) {
    enum class TimerState{
        Stopped, Paused, Running
    }
    var state = TimerState.Stopped
    private var secondsRemaining: Long = lengthInSeconds
    private var timer: CountDownTimer? = null

    fun finish(){
        //val notificationSound: Uri = Uri.parse(ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.tap_sms )
        val ringtoneUri = Uri.parse(ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.finish )
        val ringtoneSound = RingtoneManager.getRingtone(context, ringtoneUri)
        val int = Intent(context, TimeIsUp::class.java)
        int.putExtra("id", id)
        context.startActivity(int)
        ringtoneSound.play();
    }

    fun getImageId(): Int{
        Log.d("DEBUG", idPicture.toString())
        return idPicture
    }

    fun startTimer(){
            state = TimerState.Running
            timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
                override fun onFinish() {
                    finish()
                    secondsRemaining = lengthInSeconds
                    state = TimerState.Stopped
                    PrefUtils.setTimerState(id, TimerState.Stopped, context)
                }

                override fun onTick(p0: Long) {
                    secondsRemaining = p0 / 1000
                }
            }.start()
    }

    fun stopTimer(){
        timer?.cancel()
    }

    fun pausedTimer(){
        timer?.cancel()
        state = TimerState.Paused
    }

    fun getRemainingSeconds(): Long{
        return secondsRemaining
    }

    fun saveData(){
        PrefUtils.setSecondsRemaining(id, secondsRemaining, context)
        PrefUtils.setTimerState(id, state, context)
    }

    fun reset(){
        state = TimerState.Stopped
        PrefUtils.setTimerState(id, state, context)
        secondsRemaining = PrefUtils.getTimerLength(id, context)
    }

    fun loadData(){
        lengthInSeconds = PrefUtils.getTimerLength(id, context)
        state = PrefUtils.getTimerState(id, context)
        secondsRemaining = if (state == TimerState.Running || state == TimerState.Paused) PrefUtils.getSecondsRemaining(id, context)
        else lengthInSeconds
    }

    fun startBackgroundAlarm(){
        val wakeUpTime = AlarmUtils.setAlarm(id, context, AlarmUtils.nowSeconds, secondsRemaining)
        PrefUtils.setAlarmSetTime(id, AlarmUtils.nowSeconds, context)
        Log.d("DEBUG", "start alarm for $id")
    }

    fun comeBackFromBackground(){
        if (state == TimerState.Running) {
            val alarmSetTime = PrefUtils.getAlarmSetTime(id, context)
            if (alarmSetTime > 0) {
                secondsRemaining -= AlarmUtils.nowSeconds - alarmSetTime
            }
            AlarmUtils.removeAlarm(id, context)
        }
    }
}