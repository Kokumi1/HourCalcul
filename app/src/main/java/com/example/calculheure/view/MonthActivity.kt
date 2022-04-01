package com.example.calculheure.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.calculheure.R
import com.example.calculheure.model.Day
import com.example.calculheure.viewModel.MonthViewModel
import java.util.*

class MonthActivity : AppCompatActivity() {

    private lateinit var monthCalendar: Calendar
    private lateinit var resultTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var additionalTextView: TextView
    private lateinit var weekButton: Button
    private lateinit var toolbar:Toolbar
    private lateinit var monthViewModel: MonthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month)

        weekButton = findViewById(R.id.month_week_button)
        weekButton.setOnClickListener {
            val intent = Intent(this, WeekActivity::class.java)
            startActivity(intent)
        }

        //Toolbar
        toolbar = findViewById(R.id.month_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //View Model
        monthViewModel = ViewModelProvider(this).get(MonthViewModel::class.java)
        val data : List<Day> = monthViewModel.getDay().value!!
        showData(data)
    }

    private fun showData(pData : List<Day>){
        var total = 0
        var additional = 0

        for(day in pData){
            total+= day.workTime()
            if(day.workTime() > 7) additional+=day.workTime()-7
        }
        totalTextView.text = total.toString()
        additionalTextView.text = additional.toString()
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
}