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

    private val lInflater = LayoutInflater.from(context)

    private class ViewHolder(view: View){
        val nameTextView = view.findViewById<TextView>(R.id.nameCard)
        val timeTextView = view.findViewById<TextView>(R.id.timeCard)
        val pictureImageView = view.findViewById<ImageView>(R.id.icon)
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

        lateinit var viewHolder: ViewHolder
        lateinit var view: View
        if (convertView == null){
            view = lInflater.inflate(R.layout.timer_item, viewGroup, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder


//            view?.findViewById<TextView>(R.id.nameCard)?.setText(position.toString())
//            view?.findViewById<TextView>(R.id.timeCard)?.setText("${if (timers[position].getRemainingSeconds()/60 < 10) "0" else ""}${timers[position].getRemainingSeconds()/60}:${if (timers[position].getRemainingSeconds() % 60 < 10) "0" else ""}${timers[position].getRemainingSeconds()%60}")
//            view?.findViewById<ImageView>(R.id.icon)?.setImageResource(timers[position].getImageId())
        }
        else{
            view = convertView
            viewHolder = view.tag as ViewHolder
//            view?.findViewById<TextView>(R.id.timeCard)?.setText("${if (timers[position].getRemainingSeconds() / 60 < 10) "0" else ""}${timers[position].getRemainingSeconds() / 60}:${if (timers[position].getRemainingSeconds() % 60 < 10) "0" else ""}${timers[position].getRemainingSeconds() % 60}")
        }
        Log.d("DEBUG", "Callig with " + position.toString())
        viewHolder.timeTextView.setText(timers[position].getRemainingSeconds().toString())
        viewHolder.nameTextView.setText(position.toString())
        viewHolder.pictureImageView.setImageResource(timers[position].getImageId())
        //viewHolder.timeTextView.setText("${if (timers[position].getRemainingSeconds() / 60 < 10) "0" else ""}${timers[position].getRemainingSeconds() / 60}:${if (timers[position].getRemainingSeconds() % 60 < 10) "0" else ""}${timers[position].getRemainingSeconds() % 60}")
        return view
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