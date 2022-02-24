package com.example.calculheure.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.calculheure.R
import java.util.*

class MonthActivity : AppCompatActivity() {

    private lateinit var monthCalendar: Calendar
    private lateinit var resultTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var additionalTextView: TextView
    private lateinit var weekButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month)

        weekButton = findViewById(R.id.month_week_button)
        weekButton.setOnClickListener {
            val intent = Intent(this, WeekActivity::class.java)
            startActivity(intent)
        }
    }
}