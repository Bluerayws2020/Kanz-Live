package com.blueray.Kanz.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.blueray.Kanz.databinding.ActivityMainBinding
import com.blueray.Kanz.helpers.HelperUtils

class MainActivity : BaseActivity() {
//    MainView
    private val viewModel : AppViewModel by viewModels()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")



//        binding.poetButton.setOnClickListener {
//            startActivity(Intent(this,LoginActivity::class.java))
//        }
//        binding.guestBtn.setOnClickListener {
//            val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
//
//
//            sharedPreferences.edit().apply {
//                putString(HelperUtils.UID_KEY, "0")
//
//
//
//
//            }.apply()            // go to home activity
//            startActivity(Intent(this,HomeActivity::class.java))
//
//        }


    }
}