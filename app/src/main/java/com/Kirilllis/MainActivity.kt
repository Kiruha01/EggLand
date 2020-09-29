package com.Kirilllis

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Kirilllis.utils.NotificationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.timer_item.view.*
import java.util.*

/*
TODO: что показывать по завершению будильника?
 */

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var context: Context
        const val countOfTimers = 3
        lateinit var listOfTimers: Array<TimerTile>
        fun createLists(){
            listOfTimers = arrayOf(
                TimerTile(0, "Пельмяшки", 10, R.drawable.teams, context),
                TimerTile(1, "Яйца", 59, R.drawable.teams, context),
                TimerTile(2, "ГРеча", 75, R.drawable.hitman, context)
            )
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
                Toast.makeText(this, view.nameCard.text.toString(), Toast.LENGTH_SHORT).show()
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
        try {
        Log.i("DEBUG", "onPause ++++++")
        for (idTimer in listOfTimers){
            if (idTimer.state == TimerTile.TimerState.Running) {
                listOfTimers[idTimer.id].stopTimer()
                listOfTimers[idTimer.id].saveData()
                listOfTimers[idTimer.id].startBackgroundAlarm()
            }
        }

            Log.i("DEBUG", "onPause -----")
        }
        catch (t: Throwable){
            Log.i("DEBUG", "onPausebnhfj -----")
        }
    }

    override fun onStop() {
        super.onStop()
        Log.i("DEBUG", "start onStop")
        NotificationUtils.showNearestNotification(listOfTimers, this)
        Log.i("DEBUG", "on Stop")
    }

    override fun onResume() {
        super.onResume()
        for (idTimer in listOfTimers.indices){
            listOfTimers[idTimer].loadData()
            listOfTimers[idTimer].comeBackFromBackground()
            if (listOfTimers[idTimer].state == TimerTile.TimerState.Running) {
                listOfTimers[idTimer].startTimer()
            }
        }
        Log.d("DEBUG", "onResume, all loaded")
        NotificationUtils.hideNotifications(2, context)
    }
}