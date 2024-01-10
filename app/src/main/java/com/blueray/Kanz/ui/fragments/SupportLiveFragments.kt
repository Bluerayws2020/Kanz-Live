package com.blueray.Kanz.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.blueray.Kanz.adapters.SupportAdapter
import com.blueray.Kanz.databinding.SupportLiveFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SupportLiveFragments : BottomSheetDialogFragment() {
    private var _binding: SupportLiveFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : SupportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SupportLiveFragmentBinding.inflate(inflater, container, false)

setUpRecyclerView()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.layoutParams.height = 400 // replace specificHeight with your desired height in pixels
        view.requestLayout() // This will make sure the layout is redrawn with the new height

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
        private fun setUpRecyclerView() {
        adapter = SupportAdapter(listOf())
          val lm = GridLayoutManager(requireContext(),2)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = lm
    }

    // Add any additional functionality for your bottom sheet here
}
