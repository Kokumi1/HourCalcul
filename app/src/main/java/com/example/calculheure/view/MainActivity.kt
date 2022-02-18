package com.example.calculheure.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.calculheure.R
import com.example.calculheure.model.Day
import com.example.calculheure.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var currentMonthButton : Button
    private lateinit var currentWeekButton: Button
    private lateinit var todayButton: Button
    private lateinit var totalHourTextView: TextView
    private lateinit var supHourTextView: TextView

    private lateinit var mMainViewModel: MainViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val data : List<Day> = ArrayList()
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        recyclerView = findViewById(R.id.main_recycler)
        recyclerView.adapter = MonthRecycler(data as ArrayList<Day>)
        mMainViewModel.getDays().observe(this){
            kotlin.run{
                data.clear()
                data.addAll(it)
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }


        currentMonthButton = findViewById(R.id.main_current_month)
        currentMonthButton.setOnClickListener { //todo: go current month
             }
        currentWeekButton = findViewById(R.id.main_current_week)
        currentWeekButton.setOnClickListener { //todo: go current week
             }
        todayButton = findViewById(R.id.main_today)
        todayButton.setOnClickListener { //todo: go update today

        }

        totalHourTextView= findViewById(R.id.main_total_hour)
        supHourTextView= findViewById(R.id.main_sup_hour)

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
            pDays[weeks[position][0]].date,totalHour,addHour)
    }

    override fun getItemCount(): Int {
        return weeks.size
    }

    class MonthHolder(view: View): RecyclerView.ViewHolder(view){

        private val dateTextView = view.findViewById<TextView>(R.id.month_cell_date)
        private val totalTextView = view.findViewById<TextView>(R.id.month_cell_total)
        private val additionalTextView = view.findViewById<TextView>(R.id.month_cell_additional)

        fun display(pBeginDay: Date, pTotalHour: Int, pAddHour: Int){
            dateTextView.text = SimpleDateFormat("dd/MM,yyyy", Locale.FRANCE).format(pBeginDay.time)
            totalTextView.text = pTotalHour.toString()
            additionalTextView.text = pAddHour.toString()
        }
    }
}