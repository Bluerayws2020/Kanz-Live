package com.blueray.Kanz.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blueray.Kanz.R
import com.blueray.Kanz.helpers.HelperUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirstSplashesScreens : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_splashes_screens)


        lifecycleScope.launch {
            delay(2000)
            var uid = HelperUtils.getUid(this@FirstSplashesScreens)

//                    user
            if (!uid.isNullOrEmpty() && uid != "0") {
                Intent(this@FirstSplashesScreens,HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@FirstSplashesScreens, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}