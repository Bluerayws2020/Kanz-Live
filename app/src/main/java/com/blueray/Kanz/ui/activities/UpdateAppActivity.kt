package com.blueray.Kanz.ui.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.ActivityUpdateAppBinding

class UpdateAppActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUpdateAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.updateButton.setOnClickListener {
            openGooglePlay()
        }
    }
    fun openGooglePlay() {
        val appPackageName = "com.blueray.Kanz" // Replace with the package name of the target app
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName"))
            startActivity(intent)
        }
    }
}