package com.blueray.Kanz.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.Kanz.adapters.FollowersAdapter
import com.blueray.Kanz.api.FollowerClick
import com.blueray.Kanz.databinding.FragmentFollowersBinding
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var adapter: FollowersAdapter
    private val mainViewModel by viewModels<AppViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getString("user_id")

        Log.d("WERTYUIO", userId.toString())
        val tabPosition = arguments?.getString("tab_position", "0") ?: "0"

        if (tabPosition == "0") {

            mainViewModel.retriveFollower(userId.toString())

            // Code for the Followers  tab
        } else if (tabPosition == "1") {
            mainViewModel.retriveFollowing(userId.toString())

            // Code for the Following tab
        }

        Log.d("FollwersssUUUIIIIDDD", userId.toString())
        getFollowing()
        getFollower()
        setUpRecyclerView()
        getUserAction()
    }

    private fun setUpRecyclerView() {

    }

    fun getFollowing() {
        mainViewModel.getFollowing().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    adapter = FollowersAdapter(
                        requireContext(),
                        result.data.result.following,
                        object : FollowerClick {
                            override fun onFollowClikcs(pos: Int) {
                                mainViewModel.retriveSetAction(
                                    result.data.result.following[pos].uid,
                                    "user",
                                    "following"
                                )

                            }

                        })
                    val lm = LinearLayoutManager(requireContext())
                    binding.followersRv.adapter = adapter
                    binding.followersRv.layoutManager = lm

                }

                is NetworkResults.Error -> {

                    Log.d("ERRRRor", result.exception.toString())
                }

                is NetworkResults.NoInternet -> TODO()
            }
        }

    }

    fun getFollower() {
        mainViewModel.getFollower().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.result.toString().isNullOrEmpty()) {
                        binding.noData.show()
                        binding.followersRv.hide()
                    } else {
                        binding.noData.hide()
                        binding.followersRv.show()
                    }

                    var followrsList = result.data.result.followers

                    adapter = FollowersAdapter(
                        requireContext(),
                        followrsList,
                        object : FollowerClick {
                            override fun onFollowClikcs(pos: Int) {
                                mainViewModel.retriveSetAction(
                                    followrsList[pos].uid,
                                    "user",
                                    "following"
                                )
                            }
                        })

//                    if (result.data.data.isNullOrEmpty()) {
//                        binding.noData.show()
//                        binding.followersRv.hide()
//                    } else {
//                        binding.noData.hide()
//                        binding.followersRv.show()
//                    }
//
//                    var followrsList = result.data.data[0]
//
//                    adapter = FollowersAdapter(
//                        requireContext(),
//                        result.data.data,
//                        object : FollowerClick {
//                            override fun onFollowClikcs(pos: Int) {
//                                mainViewModel.retriveSetAction(
//                                    result.data.data[pos].uid,
//                                    "user",
//                                    "following"
//                                )
//                            }
//                        })
                    val lm = LinearLayoutManager(requireContext())
                    binding.followersRv.adapter = adapter
                    binding.followersRv.layoutManager = lm

                }

                is NetworkResults.Error -> {

                    Log.d("ERRRRor", result.exception.toString())
                }

                is NetworkResults.NoInternet -> TODO()
            }
        }

    }

    private fun getUserAction() {

        mainViewModel.getSetAction().observe(viewLifecycleOwner) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status == 200) {
                        Toast.makeText(
                            requireContext(),
                            result.data.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    } else {
                        Toast.makeText(
                            requireContext(),
                            result.data.msg.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }

                is NetworkResults.Error -> {
                    Toast.makeText(
                        requireContext(),
                        result.exception.printStackTrace().toString(),
                        Toast.LENGTH_LONG
                    ).show()

                    result.exception.printStackTrace()
                }

                else -> {}
            }
        }
    }


}