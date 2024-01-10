package com.blueray.Kanz.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueray.Kanz.databinding.FragmentUploadVideosBinding
import com.blueray.Kanz.ui.activities.HomeActivity

class UploadVideosFragment : Fragment() {

    private lateinit var binding : FragmentUploadVideosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadVideosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).hideBottomNav()
    }


}