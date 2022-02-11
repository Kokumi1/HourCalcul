package com.example.calculheure.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculheure.R
import com.example.calculheure.model.Day
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}
class MonthRecycler(private val pDays : ArrayList<Day>) : RecyclerView.Adapter<MonthRecycler.MonthHolder>(){
    private var weeks = Array(5) { IntArray(2) }

    init {
        var noWeek = 0
        var firstDay: Date? = null
        for( day: Day in pDays){
            val c = Calendar.getInstance()
            c.time = day.date


            when(c.get(Calendar.DAY_OF_WEEK)){
                Calendar.MONDAY -> if(c.get(Calendar.DAY_OF_WEEK) == c.firstDayOfWeek) firstDay = day.date

                Calendar.TUESDAY ->if(c.get(Calendar.DAY_OF_WEEK) == c.firstDayOfWeek) firstDay = day.date

                Calendar.WEDNESDAY ->if(c.get(Calendar.DAY_OF_WEEK) == c.firstDayOfWeek) firstDay = day.date

                Calendar.THURSDAY ->if(c.get(Calendar.DAY_OF_WEEK) == c.firstDayOfWeek) firstDay = day.date

                Calendar.FRIDAY->if(c.get(Calendar.DAY_OF_WEEK) == c.firstDayOfWeek) firstDay = day.date

                Calendar.SATURDAY->if(c.get(Calendar.DAY_OF_WEEK) == c.firstDayOfWeek) firstDay = day.date

                Calendar.SUNDAY->{
                    if(c.get(Calendar.DAY_OF_WEEK) == c.firstDayOfWeek) firstDay = day.date

                    weeks[noWeek] = intArrayOf(firstDay!!.day,day.date.day)
                    noWeek++

                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.month_cell,parent,false)
        return MonthHolder(view)
    }

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {
        var totalHour = 0
        var addHour = 0
        for(i in weeks[position][0]..weeks[position][1]){
            totalHour += pDays[i].workTime()
            if(pDays[i].workTime() > 7) addHour+= pDays[i].workTime() - 7
        }
        holder.display(
            pDays[weeks[position][0]].date, pDays[weeks[position][1]].date,
        totalHour,addHour)
    }

    override fun getItemCount(): Int {
        return weeks.size
    }

    class MonthHolder(view: View): RecyclerView.ViewHolder(view){

        fun display(pbeginDay: Date, pEndDay: Date, pTotalHour: Int, pAddHour: Int){

        }
    }
}