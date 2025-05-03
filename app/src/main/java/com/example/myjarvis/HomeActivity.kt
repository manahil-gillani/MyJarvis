package com.example.myjarvis

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val historyContainer = findViewById<LinearLayout>(R.id.historyContainer)

        val fakeHistory = listOf(
            "I need some UI inspirations ...",
            "What's the weather like today?",
            "Remind me to drink water"
        )

        historyContainer.removeAllViews()
        for (msg in fakeHistory.takeLast(3)) {
            val view = layoutInflater.inflate(R.layout.item_chat_history, historyContainer, false)
            view.findViewById<TextView>(R.id.messageText).text = msg
            historyContainer.addView(view)
        }
    }
}
