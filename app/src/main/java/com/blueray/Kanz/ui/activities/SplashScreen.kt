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
import com.blueray.Kanz.videoliveeventsample.BaseApplication
import com.blueray.Kanz.videoliveeventsample.util.PrefManager
import com.bumptech.glide.Glide
import com.sendbird.live.AuthenticateParams
import com.sendbird.live.SendbirdLive
import com.sendbird.live.videoliveeventsample.util.EventObserver

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.logging.Handler

class SplashScreen : AppCompatActivity() {
    var uid = ""
    var token=""
    private val SPLASH_DURATION = 4500L


    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {

        HelperUtils.setDefaultLanguage(this@SplashScreen, "ar")
        HelperUtils.setLang(this@SplashScreen, "ar")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val loadingImageView = findViewById<ImageView>(R.id.imageViewSplash)
        Glide.with(this)
            .asGif()
            .load(R.drawable.kinz2)
            .into(loadingImageView)

        uid = HelperUtils.getUid(this@SplashScreen)
        token=HelperUtils.getUserToken(this@SplashScreen)

//        lifecycleScope.launch{
//            delay(SPLASH_DURATION)
//            if (!token.isNullOrEmpty() && token != "0") {
//                val intent =  Intent(this@SplashScreen,HomeActivity::class.java)
//                startActivity(intent)
//                finish()}
//            else {
//                val intent = Intent(this@SplashScreen, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//
//        }
//        android.os.Handler().postDelayed({
//            loadingImageView.visibility = View.INVISIBLE
//        }, 2500)
//
//
//


        uid = HelperUtils.getUid(this@SplashScreen)

        Log.d("TEEEESTTTT",uid)
        lifecycleScope.launch {
            delay(2000)
            if (token == null || token == ""){
                val intent = Intent(
                    this@SplashScreen,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }else {


                val intent = Intent(
                    this@SplashScreen,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }




        }

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