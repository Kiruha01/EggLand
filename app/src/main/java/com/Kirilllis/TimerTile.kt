package com.Kirilllis


class TimerTile(val name: String, val lengthInSeconds: Long, val idPicture: Int) {
    enum class TimerState{
        Stopped, Paused, Running
    }

    private var secondsRemaining: Long = 0

    fun startTimer(n: Int){

    }

    fun saveData(){

    }
}