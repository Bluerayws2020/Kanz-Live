package com.blueray.Kanz.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.LayoutBottomSheetBinding
import com.blueray.Kanz.hlsdemo.common.meeting.activity.CreateOrJoinActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: LayoutBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutBottomSheetBinding.inflate(inflater, container, false)
        navController = findNavController()


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.liveBtn.setOnClickListener{
            startActivity(Intent(requireContext(), CreateOrJoinActivity::class.java))
//            navController.navigate(R.id.yourChannelFragment)

        }

        binding.vidBtn.setOnClickListener{
            startActivity(Intent(requireContext(),UploadeVedio::class.java))

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Add any additional functionality for your bottom sheet here
}
