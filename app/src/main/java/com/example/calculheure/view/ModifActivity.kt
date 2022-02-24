package com.example.calculheure.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculheure.R

class ModifActivity : AppCompatActivity() {

    private lateinit var dayTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var roadEditText: EditText
    private lateinit var loadEditText: EditText
    private lateinit var validButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif)

        validButton = findViewById(R.id.modif_confirm)
        validButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)

            startActivity(intent)
        }


    }
}