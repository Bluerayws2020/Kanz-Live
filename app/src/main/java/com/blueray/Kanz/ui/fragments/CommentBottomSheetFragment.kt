package com.blueray.Kanz.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.FragmentCommentsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCommentsBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCommentsBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener { dialog ->
                val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.background = ContextCompat.getDrawable(requireContext(), R.drawable.buttom_sheet_back)
            }
        }
    }
}