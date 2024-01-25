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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.blueray.Kanz.R
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.videoliveeventsample.BaseApplication
import com.blueray.Kanz.videoliveeventsample.util.PrefManager
import com.bumptech.glide.Glide
import com.sendbird.live.AuthenticateParams
import com.sendbird.live.SendbirdLive
import com.sendbird.live.videoliveeventsample.util.EventObserver

import android.os.Handler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class SplashScreen : AppCompatActivity() {
var uid = ""
    var token=""
    private val SPLASH_DURATION = 6000L


    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {

        HelperUtils.setDefaultLanguage(this@SplashScreen, "ar")
        HelperUtils.setLang(this@SplashScreen, "ar")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        uid = HelperUtils.getUid(this@SplashScreen)
        token=HelperUtils.getUserToken(this@SplashScreen)
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val imageViewSplash = findViewById<ImageView>(R.id.imageViewSplash)

        Glide.with(this)
            .asGif()
            .load(R.drawable.kinz2) // Replace with your GIF resource
            .into(imageViewSplash)

        // Delayed execution for 2 seconds
        Handler().postDelayed({
                        if (!token.isNullOrEmpty() && token != "0") {
                val intent =  Intent(this@SplashScreen,HomeActivity::class.java)
                startActivity(intent)
                finish()}
            else {
                val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_DURATION)



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

//                    user

        }

    }

    private fun autoAuthenticate(callback: (Boolean, String?) -> Unit) {
        val appId = "6A2870E9-4E98-4044-85DE-24DF3DDECB4B"
        val userId = HelperUtils.getUid(this)
        val accessToken = "27ef004db2ee6dcb0b628ef56229a072122a408c"

        if (appId == null || userId == null) {
            callback.invoke(false, null)
            return
        }

        val params = AuthenticateParams(userId, accessToken)
        SendbirdLive.authenticate(params) { user, e ->
            if (e != null || user == null) {
                callback.invoke(false, "${e?.message}")
                return@authenticate
            }
            callback.invoke(true, null)
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