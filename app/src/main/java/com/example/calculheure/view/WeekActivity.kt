package com.example.calculheure.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.calculheure.R

class WeekActivity : AppCompatActivity() {

    private lateinit var leftImageButton: ImageButton
    private lateinit var rightImageButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week)

        leftImageButton = findViewById(R.id.week_left_arrow)
        leftImageButton.setOnClickListener{

        }
        rightImageButton = findViewById(R.id.week_right_arrow)
        rightImageButton.setOnClickListener{

        }

    }
}