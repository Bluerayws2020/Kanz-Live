package com.blueray.Kanz.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.blueray.Kanz.R
import com.blueray.Kanz.helpers.HelperUtils

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class SplashScreen : AppCompatActivity() {
var uid = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        HelperUtils.setDefaultLanguage(this@SplashScreen, "ar")
        HelperUtils.setLang(this@SplashScreen, "ar")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        HelperUtils.setDefaultLanguage(this@SplashScreen, "ar")
//        HelperUtils.setLang(this@SplashScreen, "ar")
//        startActivity(Intent(this@SplashScreen,HomeActivity::class.java))
uid = HelperUtils.getUid(this@SplashScreen)

        Log.d("TEEEESTTTT",uid)
                lifecycleScope.launch {
                    delay(2000)

//                    user
                    if (!uid.isNullOrEmpty() && uid != "0") {
                      Intent(this@SplashScreen,HomeActivity::class.java)
                        startActivity(intent)
                        finish()}


                    else {
                        val intent = Intent(this@SplashScreen, LoginActivity::class.java)
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