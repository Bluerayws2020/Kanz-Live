package com.blueray.Kanz.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.Kanz.adapters.FollowersFollowingAdapter
import com.blueray.Kanz.adapters.MyFollowersFollowingAdapter
import com.blueray.Kanz.api.FollowerClick
import com.blueray.Kanz.databinding.FragmentFollowersBinding
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.DropDownModel
import com.blueray.Kanz.model.FollowersFollowingResult
import com.blueray.Kanz.model.FollowingList
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.viewModels.AppViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FollowersFollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var adapter: MyFollowersFollowingAdapter
    private lateinit var adapterr: MyFollowersFollowingAdapter
    private lateinit var adapter2: FollowersFollowingAdapter
    private val mainViewModel by viewModels<AppViewModel>()
    var tabPosition = "0"
    var userId: String? = ""
    var type: String? = ""
    lateinit var mainlist: MutableLiveData<FollowersFollowingResult>

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


        mainlist = MutableLiveData<FollowersFollowingResult>()
        if (type == "myAccount") {
            mainViewModel.retriveMyFollowingFollower()
            getFollowersFollowing()
            getUserAction()
        } else {
            mainViewModel.retriveUserFollowingFollower(userId.toString())
            getUserFollowersFollowing()
            getUserAction()
        }


    }

    fun getFollowersFollowing() {
        mainViewModel.getMyFollowingFollowers().observe(viewLifecycleOwner) { result ->

            when (result) {
                is NetworkResults.Success -> {

                    Log.e(
                        "***getFollowers",
                        result.data.results.followers.map { it.is_following }.toString()
                    )
                    Log.e(
                        "***getFollowing",
                        result.data.results.following.map { it.user_name }.toString()
                    )

                    if (result.data.results.following == null) {
                        binding.noData.show()
                        binding.followersRv.hide()

                    } else {
                        binding.noData.hide()
                        binding.followersRv.show()

                    }

                    mainlist.value = result.data.results
                    if (type == "myAccount") {

//                        Log.d("***", list[0].user_name + "  " + list[0].is_following)
//                        Log.d("***", list[1].user_name + "  " + list[1].is_following)
//                        Log.d("***", list[2].user_name + "  " + list[2].is_following)
//                        Log.d("***",  "  " )
                        if (tabPosition == "1") {
                            setAdapter2()
                            setAdapter1()

                        } else {
                            setAdapter1()
                            setAdapter2()

                        }
                    }

                }

                is NetworkResults.Error -> {
                    Log.d("ERRRRososr", result.exception.toString())
                }

                is NetworkResults.NoInternet -> TODO()
            }
        }

    }

    fun setAdapter1() {

        mainlist.observe(viewLifecycleOwner){
            Log.d("***1", it.following.toString())
            adapter = MyFollowersFollowingAdapter(
                requireContext(),
                it.following,
                object : FollowerClick {
                    override fun onFollowClikcs(pos: Int) {
                        GlobalScope.launch {
                            mainViewModel.retriveSetAction(
                                it.following[pos].uid,
                                "user",
                                "following"
                            )
                            delay(200)
                            mainViewModel.retriveMyFollowingFollower()

                        }
                    }
                })
            val lm = LinearLayoutManager(requireContext())
            binding.followersRv.adapter = adapter
            binding.followersRv.layoutManager = lm
        }
    }

    fun setAdapter2() {

        mainlist.observe(viewLifecycleOwner) {
            Log.d("***2", it.followers.toString())
            adapterr = MyFollowersFollowingAdapter(
                requireContext(),
                it.followers,
                object : FollowerClick {
                    override fun onFollowClikcs(pos: Int) {
                        GlobalScope.launch {
                            mainViewModel.retriveSetAction(
                                it.followers[pos].uid,
                                "user",
                                "following"
                            )
                            delay(200)
                            mainViewModel.retriveMyFollowingFollower()

                        }
                    }
                })
            val lm = LinearLayoutManager(requireContext())
            binding.followersRv.adapter = adapterr
            binding.followersRv.layoutManager = lm
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

    private fun getUserFollowersFollowing() {
        mainViewModel.getUserFollowingFollowers().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
//                    Log.d("THISLISTTT", result.data.results.toString())
                    if (result.data.results.following == null) {
                        binding.noData.show()
                        binding.followersRv.hide()
                    } else {
                        binding.noData.hide()
                        binding.followersRv.show()

                    }
                    var list = result.data.results.followers

                    if (tabPosition == "1") {
                        list = result.data.results.following
                    } else {
                        list = result.data.results.followers
                    }

                    list.mapIndexed { index, listItem ->

                        if (type == "partition") {

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
                                        mainViewModel.retriveUserFollowingFollower(userId.toString())
                                        getFollowersFollowing()
                                        Log.e("fofofofods", list[index].is_following)
                                    }
                                })
                            Log.d("typetype", type.toString())
                            Log.d("list missing !!!!", list.toString())
                            val lm = LinearLayoutManager(requireContext())
                            binding.followersRv.adapter = adapter2
                            binding.followersRv.layoutManager = lm
                        }
                    }


                }

                is NetworkResults.Error -> {

                }

                else -> {

                }
            }
        }
    }


}