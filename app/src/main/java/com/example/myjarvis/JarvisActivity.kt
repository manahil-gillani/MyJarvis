package com.example.myjarvis

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView

class JarvisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jarvis)

        val jarvisAnim = findViewById<LottieAnimationView>(R.id.jarvisAnimation)

        // Optional: control manually
        jarvisAnim.playAnimation()
        // jarvisAnim.pauseAnimation()
        // jarvisAnim.cancelAnimation()
    }
}
