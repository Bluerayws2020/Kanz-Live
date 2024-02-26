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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.blueray.Kanz.R
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel
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
    var version=""
    private val SPLASH_DURATION = 4500L

    private val viewModel by viewModels<AppViewModel>()

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {

        HelperUtils.setDefaultLanguage(this@SplashScreen, "ar")
        HelperUtils.setLang(this@SplashScreen, "ar")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val loadingImageView = findViewById<ImageView>(R.id.imageViewSplash)


        uid = HelperUtils.getUid(this@SplashScreen)
        token = HelperUtils.getUserToken(this@SplashScreen)
        if (uid.isEmpty() || uid == "0"){

            val intent =
                Intent(
                    this@SplashScreen,
                    LoginActivity::class.java
                )
            startActivity(intent)
            finish()
        }else {

            val intent  = Intent(
                this@SplashScreen,
                HomeActivity::class.java
            )
            startActivity(intent)
            finish()
        }





    }

}