package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.blueray.Kanz.R
import com.blueray.Kanz.helpers.HelperUtils
import com.bumptech.glide.Glide

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.logging.Handler

class SplashScreen : AppCompatActivity() {
var uid = ""
    var token=""
    private val SPLASH_DURATION = 4500L
    override fun onCreate(savedInstanceState: Bundle?) {

        HelperUtils.setDefaultLanguage(this@SplashScreen, "ar")
        HelperUtils.setLang(this@SplashScreen, "ar")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val loadingImageView = findViewById<ImageView>(R.id.loadingImageView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.splashscreen)
            .into(imageView)
        Glide.with(this@SplashScreen)
            .asGif()
            .load(R.drawable.loadingsplash)
            .into(loadingImageView)
        uid = HelperUtils.getUid(this@SplashScreen)
        token=HelperUtils.getUserToken(this@SplashScreen)

        lifecycleScope.launch{
            delay(SPLASH_DURATION)
            if (!token.isNullOrEmpty() && token != "0") {
                val intent =  Intent(this@SplashScreen,HomeActivity::class.java)
                startActivity(intent)
                finish()}
            else {
                val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        android.os.Handler().postDelayed({
            loadingImageView.visibility = View.INVISIBLE
        }, 2500)







    }
    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = applicationContext.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


}