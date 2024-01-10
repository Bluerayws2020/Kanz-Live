package com.blueray.Kanz.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.Kanz.R
import com.blueray.Kanz.helpers.HelperUtils

class RegistrationOtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_otp)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")
    }
}