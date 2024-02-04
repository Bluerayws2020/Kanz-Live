package com.blueray.Kanz.zegoCload

import android.R
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.Kanz.databinding.ActivityCreateLiveEventtBinding
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment


class CreateLiveEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateLiveEventtBinding


    private val mainViewModel by viewModels<AppViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.blueray.Kanz.R.layout.activity_create_live_eventt)

        addFragment()

    }


    private fun addFragment() {
        val appID: Long = 2065164494
        val appSign = "02d971b331c5ae3d500b4e1e4071d7a6418b34d55ad6cacdf3bc2761324e9eb6"
        val userID = "2"
        val userName = "qusai"

        val isHost = true
        val liveID = "test_live_id"

        val config: ZegoUIKitPrebuiltLiveStreamingConfig = if (isHost) {
            ZegoUIKitPrebuiltLiveStreamingConfig.host()
        } else {
            ZegoUIKitPrebuiltLiveStreamingConfig.audience()
        }
        val fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
            appID, appSign, userID, userName, liveID, config
        )
        supportFragmentManager.beginTransaction()
            .replace(com.blueray.Kanz.R.id.fragment_container, fragment)
            .commitNow()
    }



}