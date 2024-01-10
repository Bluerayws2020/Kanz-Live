package com.blueray.Kanz.ui.fragments




import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueray.Kanz.databinding.CommentLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentsBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: CommentLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommentLayoutBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.liveBtn.setOnClickListener{
//            startActivity(Intent(requireContext(),CreateLiveEventActivity::class.java))
//
//        }
//
//        binding.vidBtn.setOnClickListener{
//            startActivity(Intent(requireContext(), UploadeVedio::class.java))
//
//        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Add any additional functionality for your bottom sheet here
}
