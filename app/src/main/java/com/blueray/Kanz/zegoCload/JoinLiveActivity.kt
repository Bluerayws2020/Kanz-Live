package com.blueray.Kanz.zegoCload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.blueray.Kanz.R
import com.blueray.Kanz.helpers.HelperUtils
import com.zegocloud.uikit.prebuilt.livestreaming.*


class JoinLiveActivity : AppCompatActivity() {
    private  var modifiedString:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_live)

        addFragment()

    }

    private fun addFragment() {
        val appID: Long = 1099604494
        val appSign = "f192aaef99c7b160abbee0fce1675ff31ed5d4a0802a6e7051945687ab8f9fb6"
        val userID = HelperUtils.getUid(this)
        val userName = HelperUtils.getUserName(this)

        val isHost = false
        val liveID = intent.getStringExtra("roomId")

        val config: ZegoUIKitPrebuiltLiveStreamingConfig = if (isHost) {
            ZegoUIKitPrebuiltLiveStreamingConfig.host(true)
        } else {
            ZegoUIKitPrebuiltLiveStreamingConfig.audience(true)
        }
        val fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
            appID, appSign, userID, userName, liveID, config
        )
        supportFragmentManager.beginTransaction()
            .replace(com.blueray.Kanz.R.id.fragment_container22, fragment)
            .commitNow()
    }
}