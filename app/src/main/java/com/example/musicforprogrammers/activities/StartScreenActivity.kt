package com.example.musicforprogrammers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.musicforprogrammers.R
import com.example.musicforprogrammers.views.MFPTextView

class StartScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_screen)

        val button = findViewById<Button>(R.id.button_touch)
        val textView = findViewById<MFPTextView>(R.id.hello)

        button.setOnClickListener {
            textView.text = "Clicked!"
        }
    }
}