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
import com.blueray.Kanz.adapters.FollowersFollowingAdapter
import com.blueray.Kanz.adapters.MyFollowersFollowingAdapter
import com.blueray.Kanz.api.FollowerClick
import com.blueray.Kanz.databinding.FragmentFollowersBinding
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel

class FollowersFollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var adapter: MyFollowersFollowingAdapter
    private lateinit var adapter2: FollowersFollowingAdapter
    private val mainViewModel by viewModels<AppViewModel>()
    var tabPosition = "0"
    var userId: String? = ""
    var type: String? = ""

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


        userId = arguments?.getString("user_id")
        type = arguments?.getString("type")
        tabPosition = arguments?.getString("tab_position").toString()
        Log.d("***", userId.toString())
        Log.d("***", type.toString())
        if (type == "myAccount")
            mainViewModel.retriveFollowingFollower(userId.toString())
        else
            mainViewModel.retriveUserFollowingFollower(userId.toString())

        getFollowersFollowing()
        getUserAction()
    }

    fun getFollowersFollowing() {
        mainViewModel.getFollowingFollowers().observe(viewLifecycleOwner) { result ->
            Log.e("***getFollowersFollowing", result.toString())
            when (result) {
                is NetworkResults.Success -> {
                    Log.e("***getFollowersFollowing", result.data.results.toString())
                    if (result.data.results.following.isNullOrEmpty()) {
                        binding.noData.show()
                        binding.followersRv.hide()
                    } else {
                        binding.noData.hide()
                        binding.followersRv.show()
                    }

                    var list = result.data.results.followers

                    if (tabPosition == "1")
                        list = result.data.results.following


                    list.mapIndexed { index, listItem ->

                        if (listItem.is_following.isNullOrEmpty()) {

                            adapter = MyFollowersFollowingAdapter(
                                requireContext(),
                                list,
                                object : FollowerClick {
                                    override fun onFollowClikcs(pos: Int) {
                                        mainViewModel.retriveSetAction(
                                            list[pos].uid,
                                            "user",
                                            "following"
                                        )

                                        mainViewModel.retriveFollowingFollower(userId.toString())
                                        getFollowersFollowing()
                                    }
                                })

                            val lm = LinearLayoutManager(requireContext())
                            binding.followersRv.adapter = adapter
                            binding.followersRv.layoutManager = lm
                        } else {

                            adapter2 = FollowersFollowingAdapter(
                                requireContext(),
                                list,
                                object : FollowerClick {
                                    override fun onFollowClikcs(pos: Int) {
                                        mainViewModel.retriveSetAction(
                                            list[pos].uid,
                                            "user",
                                            "following"
                                        )
                                        mainViewModel.retriveFollowingFollower(userId.toString())
                                        getFollowersFollowing()
                                    }
                                })
                            val lm = LinearLayoutManager(requireContext())
                            binding.followersRv.adapter = adapter2
                            binding.followersRv.layoutManager = lm
                        }


                    }


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
            Log.e("***getUserAction", result.toString())
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.msg == 200) {
                        Toast.makeText(
                            requireContext(),
                            result.data.msg.message,
                            Toast.LENGTH_LONG
                        ).show()


                    } else {
                        Toast.makeText(
                            requireContext(),
                            result.data.msg.message.toString(),
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