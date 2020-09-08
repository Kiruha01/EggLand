package com.Kirilllis

import android.util.Log
import android.view.View
import android.widget.TextView

class TimerTile(val name: String, val lengthInSeconds: Long, val idPicture: Int) {
    private var secondsRemaining: Long = 0

    fun startTimer(n: Int){
        thatview?.findViewById<TextView>(R.id.nameCard)?.text = "Foo Bar!" + n.toString()
        Log.d("DEBUG", thatview?.findViewById<TextView>(R.id.nameCard)?.text.toString())
    }

    fun saveData(){

    }
}