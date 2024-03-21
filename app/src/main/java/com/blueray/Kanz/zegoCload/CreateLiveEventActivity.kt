package com.blueray.Kanz.zegoCload

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.Kanz.databinding.ActivityCreateLiveEventtBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment


class CreateLiveEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateLiveEventtBinding
    private val viewModel: AppViewModel by viewModels()
    private val mainViewModel by viewModels<AppViewModel>()
    private lateinit var  liveId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.blueray.Kanz.R.layout.activity_create_live_eventt)
        liveId = PrepareLivees.liveId.toString()
        addFragment()

    }


    private fun addFragment() {
        val appID: Long = 1022380921
        val appSign = "bc2ce2e37147ae5341a956505bb0fca73ac1c2a16025425d1c9c91938f3f225d"
        val userID = HelperUtils.getUid(this)
        val userName = HelperUtils.getUserName(this)

        val isHost = true
        val liveID = liveId


        val config: ZegoUIKitPrebuiltLiveStreamingConfig = if (isHost) {
            ZegoUIKitPrebuiltLiveStreamingConfig.host(true)
        } else {
            ZegoUIKitPrebuiltLiveStreamingConfig.audience(true)
        }
        val fragment = ZegoUIKitPrebuiltLiveStreamingFragment.newInstance(
            appID, appSign, userID, userName, liveID, config
        )
        supportFragmentManager.beginTransaction()
            .replace(com.blueray.Kanz.R.id.fragment_container, fragment)
            .commitNow()
    }






}