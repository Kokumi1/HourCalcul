package com.example.calculheure.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week)

        leftImageButton = findViewById(R.id.week_left_arrow)
        leftImageButton.setOnClickListener{

        }
        rightImageButton = findViewById(R.id.week_right_arrow)
        rightImageButton.setOnClickListener{

        }
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

        //View Model
        val data : ArrayList<Day> = ArrayList()
        weekViewModel = ViewModelProvider(this).get(WeekViewModel::class.java)


        //RecyclerView
        weekRecyclerView = findViewById(R.id.week_recycler)
        weekRecyclerView.adapter = WeekRecycler(data,this)
        weekViewModel.getDays().observe(this){
            kotlin.run {
                data.clear()
                data.addAll(it)
                weekRecyclerView.adapter!!.notifyDataSetChanged()
                showData(data)
            }
        }
    }

    private fun showData(pData : List<Day>){
        var totalHour= 0; var morning= 0; var afternoon= 0; var breakTime= 0; var road= 0
        var loading= 0; var additional = 0

        for(day in pData){
            totalHour+=day.workTime()
            if(day.workTime() > 7) additional+=day.workTime()-7
            loading += day.loading
        }
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

    class WeekRecycler(private val pData: List<Day>, private val pContext: Context) : RecyclerView.Adapter<WeekRecycler.WeekViewHolder>(){

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
                dayTextView.text = pDay.date.toString()
                loadTextView.text = pDay.loading.toString()
                roadTextView.text = pDay.travel.toString()
                hourTextView.text = pDay.workTime().toString()
                if(pDay.dayType != "work") statusTextView.visibility= View.VISIBLE

                button.setOnClickListener {
                    val intent= Intent(pContext,ModifActivity::class.java)
                    pContext.startActivity(intent) }

                recycler.adapter= WeekWorksiteRecycler(pDay.worksite,this)
                recycler.layoutManager =
                    LinearLayoutManager(pContext,LinearLayoutManager.HORIZONTAL,false)
            }

            override fun displayTotalData(pBreakTime : Int){
                breakTextView.text = pBreakTime.toString()
            }

        }
    }


    class WeekWorksiteRecycler(private val pData: List<Worksite?>, private val pWeekViewHolder: WeekRecycler.WeekViewHolder)
        : RecyclerView.Adapter<WeekWorksiteRecycler.WeekWorksiteViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekWorksiteViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.week_worksite_cell,parent,false)
            return WeekWorksiteViewHolder(view, pWeekViewHolder)
        }

        override fun onBindViewHolder(holder: WeekWorksiteViewHolder, position: Int) {
            holder.display(pData[position]!!,position)
        }

        override fun getItemCount(): Int {
            return pData.size
        }

        class WeekWorksiteViewHolder(view: View,val pWeekViewHolder: WeekRecycler.WeekViewHolder): RecyclerView.ViewHolder(view){

            private var worksiteTextView = view.findViewById<TextView>(R.id.week_worksite_worksite)
            private var cityTextView = view.findViewById<TextView>(R.id.week_worksite_city)
            private var workTextView = view.findViewById<TextView>(R.id.week_worksite_work)
            private var morningTextView = view.findViewById<TextView>(R.id.week_worksite_morning)
            private var afternoonTextView = view.findViewById<TextView>(R.id.week_worksite_afternoon)

            fun display(worksite: Worksite, position: Int){
                worksiteTextView.text = position.toString()
                cityTextView.text = worksite.city
                workTextView.text = worksite.work
                morningTextView.text = worksite.aM.toString()
                afternoonTextView.text= worksite.pM.toString()

                pWeekViewHolder.displayTotalData(worksite.breakTime)

            }
        }
    }
    private interface TransferData{
        fun displayTotalData(pBreakTime: Int)
    }
}