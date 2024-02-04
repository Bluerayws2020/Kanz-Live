package com.blueray.Kanz.videoliveeventsample.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blueray.Kanz.databinding.ActivitySplashBinding
import com.blueray.Kanz.videoliveeventsample.BaseApplication
import com.sendbird.live.AuthenticateParams
import com.sendbird.live.SendbirdLive

import com.sendbird.live.videoliveeventsample.util.EventObserver
import com.blueray.Kanz.videoliveeventsample.util.PrefManager
import com.blueray.Kanz.videoliveeventsample.util.showToast

class SplashActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)
//        (application as BaseApplication).initResultLiveData.observe(this, EventObserver {
//            if (it) {
//                autoAuthenticate { isSucceed, e ->
//                    if (e != null) showToast(e)
//                    val intent = if (isSucceed) Intent(this, MainActivity::class.java) else Intent(this, SignInManuallyActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            } else {
//                val intent = Intent(this, SignInManuallyActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        })
    }


}