package com.Kirilllis

import android.content.Context
import android.media.RingtoneManager
import android.os.CountDownTimer
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

class TimerTile(val id: Int, val name: String, val lengthInSeconds: Long, val idPicture: Int, val context: Context) {
    enum class TimerState{
        Stopped, Paused, Running
    }
    var state = TimerState.Stopped
    private var secondsRemaining: Long = lengthInSeconds
    private lateinit var timer: CountDownTimer

    fun finish(){
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        val ringtoneSound = RingtoneManager.getRingtone(context, ringtoneUri)

        ringtoneSound.play();
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
        timer.cancel()
    }

    fun getRemainingSeconds(): Long{
        return secondsRemaining
    }

    fun saveData(){
        PrefUtils.setSecondsRemaining(id, secondsRemaining, context)
        PrefUtils.setTimerState(id, state, context)
    }

    fun loadData(){
        //PrefUtils.setTimerState(id, TimerState.Stopped, context)
        state = PrefUtils.getTimerState(id, context)
        secondsRemaining = if (state == TimerState.Running || state == TimerState.Paused) PrefUtils.getSecondsRemaining(id, context)
        else lengthInSeconds


    }
}