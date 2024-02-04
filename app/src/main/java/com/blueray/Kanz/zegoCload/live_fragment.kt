package com.blueray.Kanz.zegoCload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.FragmentHomeLivesBinding
import com.blueray.Kanz.databinding.FragmentLiveBinding
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingConfig
import com.zegocloud.uikit.prebuilt.livestreaming.ZegoUIKitPrebuiltLiveStreamingFragment

class live_fragment : Fragment() {

    private lateinit var binding : FragmentLiveBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveBinding.inflate(layoutInflater)



        return binding.root
    }


}