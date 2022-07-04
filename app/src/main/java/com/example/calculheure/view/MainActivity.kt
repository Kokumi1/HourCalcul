package com.example.calculheure.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        recyclerView.adapter = MonthRecycler(data as ArrayList<Day>,this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mMainViewModel.getDays(this).observe(this) {
            kotlin.run {
                data.clear()
                data.addAll(it)
                recyclerView.adapter!!.notifyDataSetChanged()
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
class MonthRecycler(private val pDays : ArrayList<Day>?,private val pContext: Context) : RecyclerView.Adapter<MonthRecycler.MonthHolder>(){
    private var weeks = Array(5) { IntArray(2) }

    init {
        var noWeek = 0
        var firstDay: Date? = null
        for( day: Day in pDays!!){
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
        if(pDays != null && pDays.size != 0) {
            for (i in weeks[position][0]..weeks[position][1]) {
                totalHour += pDays[i].workTime()
                if (pDays[i].workTime() > 7) addHour += pDays[i].workTime() - 7
            }
            holder.display(
                pDays[weeks[position][0]].date, totalHour, addHour, pContext
            )
        }
    }

    override fun getItemCount(): Int {
        return weeks.size
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
        fun display(pBeginDay: Date, pTotalHour: Int, pAddHour: Int, pContext: Context){
            dateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(pBeginDay.time)
            totalTextView.text =StringBuilder(pContext.getString(R.string.main_total_hour)+pTotalHour)
            additionalTextView.text = StringBuilder(pContext.getString(R.string.main_additional_hour)+pAddHour)
        }
    }
}