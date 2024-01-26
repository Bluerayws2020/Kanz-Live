package com.blueray.Kanz.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.adapters.VideoItemAdapter
import com.blueray.Kanz.api.VideoClick
import com.blueray.Kanz.databinding.FragmentVideoListBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.model.NewAppendItItems
import com.blueray.Kanz.ui.viewModels.AppViewModel
import java.util.ArrayList


class SavedVideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoListBinding
    private lateinit var videoAdapter: VideoItemAdapter
    var newArrVideoModel = ArrayList<NewAppendItItems>()
    private lateinit var navController: NavController
    var data: Int? = null

    private var currentPage = 0
    private val pageSize = 3 // Set this based on your API's page size

    private var isLinearLayout = false
    private var lastClickedPosition = 0
    var userIdes = ""

    private var noMoreData = false
    var count = 0
    var previousTotalItemCount = 0

    private val mainViewModel by viewModels<AppViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentVideoListBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
        //binding.progressBar.show()

        // mainViewModel.retriveFlagContent("save")
        isLoading = true
        binding.shimmerView.hide()
        Log.d("***", userIdes)
        mainViewModel.retriveUserVideos("9", userIdes, "0", currentPage.toString())
        setupRecyclerView()
        getMainVidos()
    }


    private fun getUserAction() {

        mainViewModel.getSetAction().observe(viewLifecycleOwner) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.msg == 200) {
                        Toast.makeText(
                            requireContext(),
                            result.data.msg.message.toString(),
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

    var isLoading = false
    var isScrolling = true

    val onScrollViewListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                if (count == 0) {
                    noMoreData = false
                }

                if (!noMoreData) {
                    isScrolling = true

                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()


                    if (!isLoading && totalItemCount <= (lastVisibleItem + 1) && totalItemCount > previousTotalItemCount && isScrolling) {

                        previousTotalItemCount = totalItemCount
                        loadMoreItems()
                        isLoading = true
                        isScrolling = false
                    }

                }

            }
        }

    }

    fun getMainVidos() {
        mainViewModel.getUserVideos().observe(viewLifecycleOwner) { result ->
            binding.progressBar.hide()

//            binding.shimmerView.stopShimmer()
//            binding.shimmerView.hide()
            when (result) {

                is NetworkResults.Success -> {

                    if (result.data.datass == null && count == 0) {
                        binding.noData.show()
                        binding.videosRv.hide()

                    } else {
                        binding.noData.hide()
                        binding.videosRv.show()
                        count += result.data.datass?.count() ?: 0

                        result.data.datass?.forEach { item ->
                            var vidLink = ""
                            if (!(item.vimeo_detials == null)) {
                                val adaptiveFile = item.vimeo_detials?.files?.firstOrNull {
                                    it.rendition == "adaptive" || it.rendition == "360"
                                }
                                vidLink = adaptiveFile?.link ?: item.file
                                Log.d("AdaptiveLink", vidLink)
                            }

                            if (item.video_actions_per_user.save == "true") {
                                newArrVideoModel.add(
                                    NewAppendItItems(
                                        item.title,
                                        item.id.toString(),
                                        item.created_at,
                                        vidLink,
                                        item.auther.uid,
                                        item.auther.username,
                                        item.vimeo_detials?.duration.toString(),
                                        item.vimeo_detials?.pictures?.base_link.toString(),

                                        firstName = item.auther.profile_data.first_name,
                                        lastName = item.auther.profile_data.last_name,
                                        type = item.auther.type,
                                        bandNam = item.auther.profile_data.band_name,
                                        userPic = item.auther.profile_data.user_picture,
                                        status = item.moderation_state,
                                        favorites = item.video_actions_per_user.favorites.toString(),
                                        userSave = item.video_actions_per_user.save.toString(),
                                        target_user = result.data.target_user,
                                        video_counts = item.video_counts,
                                        numOfFollowers = item.auther.numOfFollowers,
                                        numOfFollowing = item.auther.numOfFollowing,
                                        numOfLikes = item.auther.numOfLikes


                                    )
                                )
                            }
                        }

                        videoAdapter.notifyDataSetChanged()
                        binding.progressBar.hide()
                        isLoading = false

                    }

                }

                is NetworkResults.Error -> {
                    // Handle error case
                }

                is NetworkResults.NoInternet -> {
                    // Handle no internet case
                }
            }
        }
    }

    fun setupRecyclerView() {

        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//                    switchToGridLayout()
        videoAdapter = VideoItemAdapter(1, newArrVideoModel, object : VideoClick {
            override fun OnVideoClic(pos: List<NewAppendItItems>, position: Int) {
//
//                                val intent = Intent(context, VidInnerPlay::class.java)
//                                intent.putExtra(
//                                        "dataList",
//                                        newArrVideoModel
//                                    ) // Assuming YourDataType is Serializable or Parcelable
//                                intent.putExtra("position", position)
//
//
//
//                                startActivity(intent)

                Log.d("fairoozzz", "hello 1")

            }

            override fun OnVideoClic(position: Int) {
                val intent = Intent(context, VidInnerPlay::class.java)
                    .apply {
//                    putExtra("dataList", newArrVideoModel) // Assuming YourDataType is Serializable or Parcelable
                        putExtra("position", position)
                    }

                PartitionChannelFragment.DataHolder.itemsList = newArrVideoModel

                intent.putExtra("isMyProfile", "1")

                startActivity(intent)
                Log.d("fairoozzz", "hello 2")
                Log.d("fairoozzz", position.toString())
            }
        }, requireContext())

        val startPosition = newArrVideoModel.size

        binding.videosRv.apply {
            adapter = videoAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addOnScrollListener(this@SavedVideoFragment.onScrollViewListener)
        }
    }

    private fun loadMoreItems() {
        Log.d("****", "loadMoreItems  $noMoreData   $count")
        if (noMoreData || count == 0) {
            Log.d("****No MORE DATA ", "qwertyuiop[")
        } else {
            currentPage++
            binding.progressBar.show()
            isLoading = true
            mainViewModel.retriveUserVideos("6", userIdes, "1", currentPage.toString())
        }
    }


}

