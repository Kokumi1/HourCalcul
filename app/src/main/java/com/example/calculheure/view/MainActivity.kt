package com.example.calculheure.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var toolbar: Toolbar

    private lateinit var mMainViewModel: MainViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //View Model
        val data : List<Day> = ArrayList()
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //RecyclerView
        recyclerView = findViewById(R.id.main_recycler)
        val adapter = MonthRecycler(daysToWeek(data as ArrayList<Day>),data, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mMainViewModel.getDays(this).observe(this) {
            kotlin.run {
                data.clear()
                data.addAll(it)
                adapter.resetDays(daysToWeek(data)!!,data)
                adapter.notifyDataSetChanged()
                showData(data)
            }
        }
        //Toolbar
        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        //Button
        currentMonthButton = findViewById(R.id.main_current_month)
        currentMonthButton.setOnClickListener {
            val intent = Intent(this,MonthActivity::class.java)
            startActivity(intent)
             }
        currentWeekButton = findViewById(R.id.main_current_week)
        currentWeekButton.setOnClickListener {
            val intent = Intent(this,WeekActivity::class.java)
            startActivity(intent)
             }
        todayButton = findViewById(R.id.main_today)
        todayButton.setOnClickListener {
            val intent = Intent(this,ModifActivity::class.java)
            startActivity(intent)
        }

        totalHourTextView= findViewById(R.id.main_total_hour)
        supHourTextView= findViewById(R.id.main_sup_hour)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_home->{
                val intent = Intent(this,MonthActivity::class.java)
                startActivity(intent)
            }
            R.id.toolbar_today->{
                val intent = Intent(this,ModifActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun daysToWeek(pDays: ArrayList<Day>?) : Array<IntArray>?{
        if(pDays != null) {
            var noWeek = 0
            val weeks = Array(5) { IntArray(2) }
            val firstDay = Calendar.getInstance()

            for (day: Day in pDays) {
                val c = Calendar.getInstance()
                c.time = day.date


                if (c.get(Calendar.DAY_OF_WEEK) == c.firstDayOfWeek) firstDay.time=
                    day.date

                if(c.getActualMaximum(Calendar.DATE) == c.get(Calendar.DAY_OF_MONTH)){
                    weeks[noWeek] = intArrayOf(firstDay.get(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_MONTH))
                    Log.d("weeks","week: ${firstDay.get(Calendar.DAY_OF_MONTH)} to ${c.get(Calendar.DAY_OF_MONTH)}")
                    noWeek++
                } else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    if(firstDay == Calendar.getInstance()){
                        firstDay.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1)
                        Log.d("firstDay","no first day")
                    }
                    weeks[noWeek] = intArrayOf(firstDay.get(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_MONTH))
                    Log.d("weeks","week: ${firstDay.get(Calendar.DAY_OF_MONTH)} to ${c.get(Calendar.DAY_OF_MONTH)}")
                    noWeek++
                }

            }
            return weeks
        } else return null
    }


    private fun showData(pDays : List<Day>){
        var totalH = 0
        var supH = 0

        for(day in pDays){
            totalH += day.workTime()
            if(day.workTime() > 7) supH += day.workTime()-7
        }

        totalHourTextView.text = totalH.toString()
        supHourTextView.text = supH.toString()
    }
}

/**
 * recycler View Adapter
 */
class MonthRecycler(private var pWeeks : Array<IntArray>?, private var pDays: ArrayList<Day>?,private val pContext: Context) : RecyclerView.Adapter<MonthRecycler.MonthHolder>(){


    fun resetDays(pWeeks: Array<IntArray>, pDays: ArrayList<Day>){
        this.pWeeks = pWeeks
        this.pDays = pDays
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.month_cell,parent,false)
        return MonthHolder(view)
    }

    override fun onBindViewHolder(holder: MonthHolder, position: Int) {
        var totalHour = 0
        var addHour = 0
        if(pDays != null && pDays!!.size != 0 && pWeeks != null) {
            for (i in pWeeks!![position][0] until pWeeks!![position][1]) {
                totalHour += pDays!![i-1].workTime()
                if (pDays!![i].workTime() > 7) addHour += pDays!![i].workTime() - 7
            }
            if(pWeeks!![position][0] > 0){
                holder.display(
                    pDays!![pWeeks!![position][0]-1].date, pDays!![pWeeks!![position][1]-1].date, totalHour, addHour, pContext
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return pWeeks!!.size
    }

    /**
     * recycler View Holder
     */
    class MonthHolder(view: View): RecyclerView.ViewHolder(view){

        private val dateTextView = view.findViewById<TextView>(R.id.month_cell_date)
        private val totalTextView = view.findViewById<TextView>(R.id.month_cell_total)
        private val additionalTextView = view.findViewById<TextView>(R.id.month_cell_additional)

        /**
         * display data in cell
         */
        fun display(pBeginDay: Date, pEndDay: Date, pTotalHour: Int, pAddHour: Int, pContext: Context){
            //dateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(pBeginDay.time)
            dateTextView.text = StringBuilder(SimpleDateFormat(
                "dd", Locale.getDefault()).format(pBeginDay) + " to "
                    + SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(pEndDay))

            totalTextView.text =StringBuilder(pContext.getString(R.string.main_total_hour)+pTotalHour)
            additionalTextView.text = StringBuilder(pContext.getString(R.string.main_additional_hour)+pAddHour)
        }
    }
}