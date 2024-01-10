package com.blueray.Kanz.videoliveeventsample.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.FragmentMySettingsBinding
import com.sendbird.live.SendbirdLive
import com.blueray.Kanz.videoliveeventsample.util.PrefManager
import com.blueray.Kanz.videoliveeventsample.view.SignInManuallyActivity

class MySettingsFragment : BaseFragment<FragmentMySettingsBinding>(FragmentMySettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
//        binding.ivProfile.load(SendbirdLive.currentUser?.profileUrl) {
//            crossfade(true)
//            placeholder(R.drawable.icon_user)
//            error(R.drawable.icon_user)
//        }
        binding.tvUserId.text = SendbirdLive.currentUser?.userId ?: ""
        binding.tvAppId.text = SendbirdLive.applicationId
        binding.ivProfile.setImageResource(R.drawable.logo)
        binding.btnSignOut.setOnClickListener { signOut() }
    }

    private fun signOut() {
        SendbirdLive.deauthenticate(null)
        val prefManager = PrefManager(requireContext())
        prefManager.removeAll()
        startActivity(Intent(requireActivity(), SignInManuallyActivity::class.java))
        requireActivity().finishAffinity()
    }
}