package com.blueray.Kanz.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.Kanz.R

class SplashScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)
        val intent = Intent(this , SplashScreen::class.java)
        startActivity(intent)
        finish()
    }
}