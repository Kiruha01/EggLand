package com.Kirilllis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.timer_item.view.*

class MainActivity : AppCompatActivity() {
    val listOfTimers = arrayOf(TimerTile("Пельмяшки", 120, R.drawable.teams), TimerTile("Пельмяши", 59, R.drawable.teams), TimerTile("ГРеча", 75, R.drawable.hitman))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val girdAdapter = TimersAdapter(listOfTimers, this)
        girdView.adapter = girdAdapter
        girdView.setOnItemClickListener { adapterView, view, pos, id ->
            //listOfTimers[pos].thatview = view
            Toast.makeText(this, view.nameCard.text.toString(), Toast.LENGTH_SHORT).show()
            listOfTimers[pos].startTimer(pos)
        }
    }


}