package com.Kirilllis

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.timer_item.view.*
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var context: Context
        const val countOfTimers = 3
        lateinit var listOfTimers: Array<TimerTile>
        val listIDOfRunningTimers = arrayListOf<Int>()
        fun createLists(){
            listOfTimers = arrayOf(TimerTile(0, "Пельмяшки", 10, R.drawable.teams, context), TimerTile(1, "Пельмяши", 59, R.drawable.teams, context), TimerTile(2, "ГРеча", 75, R.drawable.hitman, context))
        }
    }

    val MainTimer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Companion.context = this
        Companion.createLists()
        val girdAdapter = TimersAdapter(listOfTimers, this)
        girdView.adapter = girdAdapter
        girdView.setOnItemClickListener { adapterView, view, pos, id ->
            if (listOfTimers[pos].state != TimerTile.TimerState.Running) {
                listOfTimers[pos].startTimer()
                //listOfTimers[pos].thatview = view
                Toast.makeText(this, view.nameCard.text.toString(), Toast.LENGTH_SHORT).show()
                listIDOfRunningTimers.add(pos)
            }
        }



        MainTimer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    runOnUiThread{
                        girdAdapter.notifyDataSetChanged()
                    }
                }
            },
            0, 1000
        )
    }

    override fun onPause() {
        super.onPause()
        for (idTimer in listIDOfRunningTimers){
            listOfTimers[idTimer].saveData()
            listOfTimers[idTimer].stopTimer()
        }
    }

    override fun onResume() {
        super.onResume()
        for (idTimer in listOfTimers.indices){
            listOfTimers[idTimer].loadData()
            if (listOfTimers[idTimer].state == TimerTile.TimerState.Running) {
                listOfTimers[idTimer].startTimer()
                listIDOfRunningTimers.add(idTimer)
            }

        }
    }
}