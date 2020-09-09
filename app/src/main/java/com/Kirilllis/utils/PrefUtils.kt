package com.Kirilllis.utils

import android.content.Context
import android.preference.PreferenceManager
import com.Kirilllis.TimerTile

class PrefUtils {
    companion object{
        fun getTimerLength(context: Context): Int{
            return 1
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.Kirilllis.EggLand.previous_timer_length"

        fun getPreviousTimerLength(id : Int, context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID + id.toString(), 0)
        }

        fun setPreviousTimerLength(id : Int, seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID + id.toString(), seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.Kirilllis.EggLand.timer_state"
        fun getTimerState(id : Int, context: Context) : TimerTile.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal =  preferences.getInt(TIMER_STATE_ID + id.toString(), 0)
            return TimerTile.TimerState.values()[ordinal]
        }

        fun setTimerState(id : Int, state: TimerTile.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID + id.toString(), ordinal)
            editor.apply()
        }
        private const val SECONDS_REMAINING = "com.Kirilllis.EggLand.seconds_remaining"
        fun getSecondsRemaining(id : Int, context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING + id.toString(), 0)
        }

        fun setSecondsRemaining(id : Int, seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING + id.toString(), seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "com.Kirilllis.EggLand.background_timer"

        fun getAlarmSetTime(id : Int, context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID + id.toString(), 0)
        }

        fun setAlarmSetTime(id : Int, time:Long, context: Context){
            val preferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
            preferences.putLong(ALARM_SET_TIME_ID + id.toString(), time)
            preferences.apply()
        }
    }
}