package com.example.polisapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.imagePolisLogo)
        val tagline = findViewById<TextView>(R.id.textTagline)

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1200

        logo.startAnimation(fadeIn)
        logo.alpha = 1f

        tagline.startAnimation(fadeIn)
        tagline.alpha = 1f

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainMenuActivity::class.java))
            finish()
        }, 2500)
    }
}
