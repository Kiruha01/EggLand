package com.Kirilllis

import android.content.Context
import android.media.RingtoneManager
import android.os.CountDownTimer
import com.Kirilllis.utils.PrefUtils
import kotlinx.android.synthetic.main.timer_item.view.*


class TimerTile(val id: Int, val name: String, val lengthInSeconds: Long, val idPicture: Int, val context: Context) {
    enum class TimerState{
        Stopped, Paused, Running
    }
    var state = TimerState.Stopped
    private var secondsRemaining: Long = lengthInSeconds
    private lateinit var timer: CountDownTimer

    fun startTimer(){
        fun finish(){
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            val ringtoneSound = RingtoneManager.getRingtone(context, ringtoneUri)

            ringtoneSound.play();
        }
        if (state != TimerState.Running) {
            state = TimerState.Running
            timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
                override fun onFinish() {
                    finish()
                    secondsRemaining = 0
                    PrefUtils.setTimerState(id, TimerState.Stopped, context)
                }

                override fun onTick(p0: Long) {
                    secondsRemaining = p0 / 1000

                }
            }.start()
        }
    }

    fun getRemainingSeconds(): Long{
        return secondsRemaining
    }

    fun pauseTimer(){

    }

    fun stopTimer(){

    }

    fun resumeTimer(){

    }

    fun saveData(){

    }

    fun loadData(){

    }
}