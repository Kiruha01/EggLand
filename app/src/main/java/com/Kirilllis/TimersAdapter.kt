package com.Kirilllis

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlin.concurrent.timer

class TimersAdapter(var timers: Array<TimerTile>, val context: Context): BaseAdapter(){

    var lInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var view = convertView
        if (convertView == null){
            view = lInflater?.inflate(R.layout.timer_item, viewGroup, false)
            view?.findViewById<TextView>(R.id.nameCard)?.setText(timers[position].name)
            view?.findViewById<TextView>(R.id.timeCard)?.setText("${if (timers[position].getRemainingSeconds()/60 < 10) "0" else ""}${timers[position].getRemainingSeconds()/60}:${if (timers[position].getRemainingSeconds() % 60 < 10) "0" else ""}${timers[position].getRemainingSeconds()%60}")
            view?.findViewById<ImageView>(R.id.icon)?.setImageResource(timers[position].idPicture)
        }
        else{
            view?.findViewById<TextView>(R.id.timeCard)?.setText("${if (timers[position].getRemainingSeconds() / 60 < 10) "0" else ""}${timers[position].getRemainingSeconds() / 60}:${if (timers[position].getRemainingSeconds() % 60 < 10) "0" else ""}${timers[position].getRemainingSeconds() % 60}")
        }
        return view!!;
    }

    override fun getItem(p0: Int): Any {
        return timers[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return timers.size
    }

}