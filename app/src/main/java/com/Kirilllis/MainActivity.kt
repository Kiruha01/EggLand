package com.Kirilllis

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.Kirilllis.utils.NotificationUtils
import com.Kirilllis.utils.PrefUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.timer_item.view.*
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var context: Context
        lateinit var listOfTimers: Array<TimerTile>
        fun createLists(){
            val tArray = context.resources.obtainTypedArray(R.array.images)
            listOfTimers = Array<TimerTile>(context.resources.getStringArray(R.array.names).size){
                TimerTile(it, context.resources.getStringArray(R.array.names)[it], PrefUtils.getTimerLength(it, context),  tArray.getResourceId(it, R.drawable.grechka), context)
            }
                tArray.recycle()

        }
    }

    val MainTimer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Companion.context = this
        Companion.createLists()
        for (i in listOfTimers.indices){
            Log.d("DEBUG", listOfTimers[i].name)
        }
        val girdAdapter = TimersAdapter(listOfTimers, this)
        girdView.adapter = girdAdapter
        girdView.setOnItemClickListener { adapterView, view, pos, id ->
            if (listOfTimers[pos].state != TimerTile.TimerState.Running) {
                listOfTimers[pos].startTimer()
                Toast.makeText(this, view.nameCard.text.toString() + " буль-буль", Toast.LENGTH_SHORT).show()
            }
            else if (listOfTimers[pos].state == TimerTile.TimerState.Running) {
                listOfTimers[pos].pausedTimer()
                Toast.makeText(this, "Слышен храп - " + view.nameCard.text.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        girdView.setOnItemLongClickListener { adapterView, view, i, l ->
            if (listOfTimers[i].state == TimerTile.TimerState.Running || listOfTimers[i].state == TimerTile.TimerState.Paused) {
                listOfTimers[i].stopTimer()
                listOfTimers[i].reset()
                girdAdapter.notifyDataSetChanged()
                Toast.makeText(
                    this,
                    "Ты перехотел " + view.nameCard.text.toString() + "?",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (listOfTimers[i].state == TimerTile.TimerState.Stopped)
            {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Введите время для " + listOfTimers[i].name + " в минутах")

                val input = EditText(this)
                input.inputType =
                    InputType.TYPE_CLASS_NUMBER
                builder.setView(input)
                builder.setPositiveButton("OK") { dialog, which ->
                    if (input.text.toString() != "") {
                        PrefUtils.setTimerLength(i, input.text.toString().toLong(), context)
                        listOfTimers[i].loadData()
                        girdAdapter.notifyDataSetChanged()
                        //m_Text = input.text.toString()
                    }
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                builder.show();
            }

            true
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
        NotificationUtils.hideNotifications(-1, context)
    }
}