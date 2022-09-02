package com.example.calculheure.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculheure.R
import com.example.calculheure.model.Day
import com.example.calculheure.model.Worksite
import com.example.calculheure.viewModel.WeekViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeekActivity : AppCompatActivity() {

    private lateinit var leftImageButton: ImageButton
    private lateinit var rightImageButton: ImageButton
    private lateinit var toolbar: Toolbar
    private lateinit var weekTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var morningTextView: TextView
    private lateinit var breakTextView: TextView
    private lateinit var roadTextView: TextView
    private lateinit var afternoonTextView: TextView
    private lateinit var loadingTextView: TextView
    private lateinit var additionalTextView: TextView
    private lateinit var weekRecyclerView: RecyclerView

    private lateinit var weekViewModel: WeekViewModel

    private val data : ArrayList<Day> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week)

        //View Model
        //val data : ArrayList<Day> = ArrayList()
        weekViewModel = ViewModelProvider(this).get(WeekViewModel::class.java)
        val date = Calendar.getInstance()

        weekTextView = findViewById(R.id.week_week)
        totalTextView=findViewById(R.id.week_totalhour)
        morningTextView = findViewById(R.id.week_morning)
        afternoonTextView = findViewById(R.id.week_afternoon)
        breakTextView = findViewById(R.id.week_break)
        roadTextView = findViewById(R.id.week_road)
        loadingTextView = findViewById(R.id.week_loading)
        additionalTextView = findViewById(R.id.week_additional)

        //Toolbar
        toolbar = findViewById(R.id.week_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //RecyclerView
        weekRecyclerView = findViewById(R.id.week_recycler)
        val adapter = WeekRecycler(data,this)
        weekRecyclerView.adapter = adapter
        weekRecyclerView.layoutManager = LinearLayoutManager(this)
        viewModelGetDays(date, adapter)


        leftImageButton = findViewById(R.id.week_left_arrow)
        leftImageButton.setOnClickListener{
            date.time = data.first().date
            date.add(Calendar.DAY_OF_YEAR, -1)
            Log.d("week navigation","date: ${data.first().date} new: ${date.time}")
            viewModelGetDays(date, adapter)
        }
        rightImageButton = findViewById(R.id.week_right_arrow)
        rightImageButton.setOnClickListener{
            date.time = data.last().date
            date.add(Calendar.DAY_OF_YEAR, 1)
            viewModelGetDays(date, adapter)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun viewModelGetDays(pDate: Calendar, pAdapter: WeekRecycler) {
        return weekViewModel.getDays(this,pDate.time).observe(this){
            kotlin.run {
                data.clear()
                data.addAll( it.sortedBy {it.date})
                pAdapter.resetDays(data)
                pAdapter.notifyDataSetChanged()
                showData(data)
            }
        }
    }

    private fun showData(pData : List<Day>){
        var totalHour= 0; var morning= 0; var afternoon= 0; var breakTime= 0; var road= 0
        var loading= 0; var additional = 0
        val firstDay = Calendar.getInstance()
        firstDay.time = pData.first().date
        val lastDay = Calendar.getInstance()
        lastDay.time = pData.last().date

        weekTextView.text = StringBuilder("${firstDay.get(Calendar.DAY_OF_MONTH)}/${firstDay.get(Calendar.MONTH)+1} to " +
                "${lastDay.get(Calendar.DAY_OF_MONTH)}/${lastDay.get(Calendar.MONTH)+1}/${lastDay.get(Calendar.YEAR)}")

        for(day in pData){
            totalHour+=day.workTime()
            if(day.workTime() > 7) additional+=day.workTime()-7
            loading += day.loading
            road += day.travel

            for(worksite in day.worksite){
                morning+= worksite!!.aM
                afternoon+=worksite.pM
                breakTime+=worksite.breakTime
            }
        }

        totalTextView.text = StringBuilder("${resources.getString(R.string.week_total_time)} $totalHour H")
        morningTextView.text = StringBuilder("${resources.getString(R.string.week_morning)} $morning H")
        afternoonTextView.text = StringBuilder( "${resources.getString(R.string.week_afternoon)} $afternoon H")
        breakTextView.text = StringBuilder("${resources.getString(R.string.week_break)} $breakTime H")
        roadTextView.text = StringBuilder("${resources.getString(R.string.week_road)} $road H")
        loadingTextView.text = StringBuilder("${resources.getString(R.string.week_loading)} $loading H")
        additionalTextView.text = StringBuilder("${resources.getString(R.string.week_additional)} $additional H")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_home->{
                val intent = Intent(this,MainActivity::class.java)
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


    class WeekRecycler(private var pData: List<Day>, private val pContext: Context) : RecyclerView.Adapter<WeekRecycler.WeekViewHolder>(){

        fun resetDays(pData: List<Day>){
            this.pData = pData
            Log.d("WeekRecycler","data reset: ${this.pData.size}")
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.week_cell,parent,false)
            return WeekViewHolder(view, pContext)
        }

        override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
            holder.display(pData[position])
        }

        override fun getItemCount(): Int {
           return pData.size
        }


        class WeekViewHolder(view: View, private val pContext: Context) : RecyclerView.ViewHolder(view),TransferData{

            private var dayTextView = view.findViewById<TextView>(R.id.week_cell_day)
            private var breakTextView = view.findViewById<TextView>(R.id.week_cell_break)
            private var statusTextView = view.findViewById<TextView>(R.id.week_cell_status)
            private var loadTextView = view.findViewById<TextView>(R.id.week_cell_load)
            private var roadTextView =view.findViewById<TextView>(R.id.week_cell_road)
            private var hourTextView = view.findViewById<TextView>(R.id.week_cell_hour)
            private var button = view.findViewById<Button>(R.id.week_cell_button)
            private var recycler =  view.findViewById<RecyclerView>(R.id.week_cell_recycler)

            fun display(pDay: Day){
                dayTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(pDay.date)
                loadTextView.text = StringBuilder("${pContext.resources.getString(R.string.modif_load)}  ${pDay.loading}")
                roadTextView.text = StringBuilder(" ${pContext.resources.getString(R.string.modif_road)} ${pDay.travel}")
                hourTextView.text = StringBuilder("${pContext.resources.getString(R.string.main_total_hour)} ${pDay.workTime()}")
                if(pDay.worksite.size == 0) statusTextView.visibility= View.VISIBLE
                if(pDay.worksite.size > 0) statusTextView.visibility= View.GONE

                button.setOnClickListener {
                    val intent= Intent(pContext,ModifActivity::class.java)
                    pContext.startActivity(intent) }

                recycler.adapter= WeekWorksiteRecycler(pDay.worksite,this, pContext)
                recycler.layoutManager =
                    LinearLayoutManager(pContext,LinearLayoutManager.HORIZONTAL,false)
            }

            override fun displayTotalData(pBreakTime : Int){
                breakTextView.text = pBreakTime.toString()
            }

        }
    }


    class WeekWorksiteRecycler(private val pData: List<Worksite?>, private val pWeekViewHolder: WeekRecycler.WeekViewHolder,
                               private val pContext: Context)
        : RecyclerView.Adapter<WeekWorksiteRecycler.WeekWorksiteViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekWorksiteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.week_worksite_cell,parent,false)
            return WeekWorksiteViewHolder(view, pWeekViewHolder, pContext)
        }

        override fun onBindViewHolder(holder: WeekWorksiteViewHolder, position: Int) {
            holder.display(pData[position]!!,position)
        }

        override fun getItemCount(): Int {
            return pData.size
        }

        class WeekWorksiteViewHolder(view: View, private val pWeekViewHolder: WeekRecycler.WeekViewHolder,
                                     private val pContext: Context): RecyclerView.ViewHolder(view){

            private var worksiteTextView = view.findViewById<TextView>(R.id.week_worksite_worksite)
            private var cityTextView = view.findViewById<TextView>(R.id.week_worksite_city)
            private var workTextView = view.findViewById<TextView>(R.id.week_worksite_work)
            private var morningTextView = view.findViewById<TextView>(R.id.week_worksite_morning)
            private var afternoonTextView = view.findViewById<TextView>(R.id.week_worksite_afternoon)

            fun display(worksite: Worksite, position: Int){
                worksiteTextView.text = StringBuilder("${position+1}")
                cityTextView.text = StringBuilder("${pContext.resources.getString(R.string.modif_cell_city)}  ${worksite.city}")
                workTextView.text = StringBuilder("${pContext.resources.getString(R.string.modif_cell_work)}  ${worksite.work}")
                morningTextView.text =StringBuilder("${pContext.resources.getString(R.string.modif_cell_morning)}  ${worksite.aM} H")
                afternoonTextView.text=StringBuilder("${pContext.resources.getString(R.string.modif_cell_afternoon)}  ${worksite.pM} H")

                pWeekViewHolder.displayTotalData(worksite.breakTime)

            }
        }
    }
    private interface TransferData{
        fun displayTotalData(pBreakTime: Int)
    }
}