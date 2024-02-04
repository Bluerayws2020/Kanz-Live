package com.blueray.Kanz.zegoCload

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    companion object{
        private var roomId:String? = null
    }
    private lateinit var binding: FragmentLiveVideossListBinding

    private val viewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveVideossListBinding.inflate(layoutInflater)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.retrieveLiveVideos()
            getLives()
        }
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
                    Log.e("LDKDJSDADADAD" , results.data.results.forYouLiveStraems.toString())
                    binding.livesRv.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.VERTICAL , false)
                 val   liveAdapterArr = results.data.results.forYouLiveStraems

                    var liveAdapter = LiveVideosListAdapter(liveAdapterArr , object : LiveCLick{
                        override fun OnLiveClick(position: Int) {
                         val roomId =   liveAdapterArr[position].room_id
                            val intent =Intent(requireContext() , JoinLiveActivity::class.java)
                            intent.putExtra("roomId" , roomId)
                            startActivity(intent)
                        }

                    })
                    binding.livesRv.adapter = liveAdapter
                    liveAdapter.notifyDataSetChanged()
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