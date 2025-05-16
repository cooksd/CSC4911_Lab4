package com.example.lab4.ui.overview

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.lab4.R
import com.example.lab4.ui.addclass.AddClassActivity

class StudentOverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_student_overview)

        val addClassButton = findViewById<Button>(R.id.addClassButton)
        addClassButton.setOnClickListener {
            val intent = Intent(this, AddClassActivity::class.java)
            startActivity(intent)
        }
    }
}