package com.blueray.Kanz.zegoCload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.Kanz.databinding.FragmentLiveVideossListBinding
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel


class LiveVideossListFragment : Fragment() {

    private lateinit var binding: FragmentLiveVideossListBinding
    var liveAdapter = LiveVideosListAdapter(listOf())
    private val viewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveVideossListBinding.inflate(layoutInflater)

        viewModel.retrieveLiveVideos()
        setUpRecyclerView()
        getLives()

        return binding.root
    }

    private fun getLives() {
        viewModel.getLiveVideos().observe(viewLifecycleOwner){
            results ->
            when(results){
                is NetworkResults.Success ->{
                    binding.livesRv.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
                    liveAdapter.list = results.data.results.forYouLiveStraems
                }
                is NetworkResults.Error ->{
                    Toast.makeText(requireContext(),results.exception.message.toString() ,Toast.LENGTH_LONG ).show()
                }

                else -> {}
            }
        }
    }

    private fun setUpRecyclerView() {


    }


}