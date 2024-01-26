package com.blueray.Kanz.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blueray.Kanz.R
import com.blueray.Kanz.adapters.VideoItemAdapter
import com.blueray.Kanz.api.VideoClick
import com.blueray.Kanz.databinding.FragmentPartitionChannelBinding
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.model.NewAppendItItems
import com.blueray.Kanz.ui.activities.FollowingAndFollowersActivity
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PartitionChannelFragment : Fragment() {
    object DataHolder {
        var itemsList: ArrayList<NewAppendItItems>? = null
    }

    private var lastFirstVisiblePosition = 0

    private var isLastPage = false
    private var isUserInteraction = false

    private lateinit var binding: FragmentPartitionChannelBinding
    private lateinit var videoAdapter: VideoItemAdapter
    var newArrVideoModel = ArrayList<NewAppendItItems>()
    private lateinit var navController: NavController

    private var currentPage = 1
    var target_user_follow_flag = ""
    private var isLinearLayout = false
    private var lastClickedPosition = 0
    var userIdes = ""
    var userName = ""
    var followFlag = ""
    private var noMoreData = false
    var count = 0
    var previousTotalItemCount = 0
    var userImg = ""
    private val mainViewModel by viewModels<AppViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPartitionChannelBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        // Retrieve the passed data
        userName = arguments?.getString("usernName").toString()
        userIdes = arguments?.getString("userIdes").toString()
        userImg = arguments?.getString("userImg").toString()
        val fullname = arguments?.getString("fullname")

        val numOfLikes = arguments?.getString("numOfLikes")
        val numOfFollowing = arguments?.getString("numOfFollowing")
        val numOfFollowers = arguments?.getString("numOfFollowers")
        target_user_follow_flag = arguments?.getString("isUserFollower").toString()

        //  binding.numFolloweing.text = numOfFollowing ?: "0"

        //   binding.numFollowers.text = numOfFollowers ?: "0"
        binding.numOfLike.text = numOfLikes ?: "0"
        binding.includeTap.title.text = fullname
        getUserAction()
        getUserProfile()
        //  mainViewModel.retriveCheckUserFolow(userIdes)
        //      getCheckFollowId()

/// inistial State


        Log.d("isFollowing?", followFlag.toString())
        // Revert changes for "Follow" state


        binding.includeTap.back.hide()


//            if (HelperUtils.getUid(requireContext()) == userIdes){
//                binding.followBtn.text =  "الغاء المتابعة"
//                binding.followBtn.setBackgroundResource(R.drawable.un_follow)
//                mainViewModel.retriveSetAction("1", "user", "following")
//
//
//            }else {
//                binding.followBtn.text = "متابعة"
//                binding.followBtn.setBackgroundResource(R.drawable.un_follow)
//                mainViewModel.retriveSetAction("1", "user", "following")
//
//
//            }

        Glide.with(requireContext()).load(userImg).placeholder(R.drawable.logo)
            .into(binding.profileImage)


        if (userName != null) {
            // Use the retrieved itemId
            // For example, load data based on this itemId or initialize views
            binding.userName.text = "@$userName"
//                binding.name.text = fullname

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.progressBar.show()
        isLoading = true
        binding.shimmerView.startShimmer()
        Log.d("***", "page: $currentPage   Page_limit: 9   Is_home: 1  user_profile_uid : $userIdes")
        mainViewModel.retriveUserVideos("3", userIdes, "0", currentPage.toString())
        mainViewModel.retriveUserProfile(userIdes)
        getMainVidos()

        Log.d("*WWEEEE**", userIdes)

        binding.followersLayout.setOnClickListener {
            val intent = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
            intent.putExtra("type", "partition")
            intent.putExtra("user_id", userIdes) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName", userName) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("followersLayout", "1") // Replace 'yourUserId' with the actual user ID
            intent.putExtra("flag", "0")

            startActivity(intent)

        }

        binding.followingLayout.setOnClickListener {
            val intent = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
            intent.putExtra("type", "partition")
            intent.putExtra("user_id", userIdes) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName", userName) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("followersLayout", "0") // Replace 'yourUserId' with the actual user ID
            intent.putExtra("flag", "1")

            startActivity(intent)
        }
//
//        // Initial layout as Grid
//        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
//        binding.videosRv.adapter = videoAdapter

    }

    private fun getUserProfile() {
        mainViewModel.getUserProfile().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    followFlag = result.data.results.is_following
                    binding.numFollowers.text = result.data.results.followers_count
                    binding.numFolloweing.text = result.data.results.following_count
                    if (followFlag == "false") {

                        binding.btnFollow.show()
                        binding.btnFollow.setOnClickListener {
                            mainViewModel.retriveSetAction(userIdes, "user", "following")
                            binding.btnUnfollow.show()
                            it.hide()
                            GlobalScope.launch {
                                delay(200)
                                mainViewModel.retriveUserProfile(userIdes)
                            }
                        }
                            binding.btnUnfollow.setOnClickListener {
                                mainViewModel.retriveSetAction(userIdes, "user", "following")
                                binding.btnFollow.show()
                                it.hide()
                                GlobalScope.launch {
                                    delay(200)
                                    mainViewModel.retriveUserProfile(userIdes)
                                }
                            }


                    } else {
                        binding.btnUnfollow.show()
                        binding.btnUnfollow.setOnClickListener {
                            mainViewModel.retriveSetAction(userIdes, "user", "following")
                            binding.btnFollow.show()
                            it.hide()
                            GlobalScope.launch {
                                delay(200)
                                mainViewModel.retriveUserProfile(userIdes)
                            }
                        }
                            binding.btnFollow.setOnClickListener {
                                mainViewModel.retriveSetAction(userIdes, "user", "following")
                                binding.btnUnfollow.show()
                                it.hide()
                                GlobalScope.launch {
                                    delay(200)
                                    mainViewModel.retriveUserProfile(userIdes)
                                }
                            }


                    }
                }

                is NetworkResults.Error -> {

                }

                else -> {}
            }
        }
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
                        Log.d("TEEEEES", userIdes)
                        Log.d("TEEEEES", userImg.toString())


//                        if(result.data.msg == "unfollowing success"){
//                            binding.followCheckbox.isChecked =  true
//                            binding.followCheckbox.text = "متابعة"
//
//                        //
//                        }else {
//                            binding.followCheckbox.isChecked =  false
//                            binding.followCheckbox.text =  "الغاء المتابعة"
//
//
//
//
//
//                        }


                    } else {
                        Toast.makeText(
                            requireContext(),
                            result.data.msg.message,
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


                    if (!isLoading && totalItemCount <= (lastVisibleItem + 1) && totalItemCount > previousTotalItemCount && isScrolling && isLastPage == false) {

                        previousTotalItemCount = totalItemCount
                        loadMoreItems()
                        isLoading = true
                        isScrolling = false
                    }

                }

            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun getMainVidos() {
        mainViewModel.getUserVideos().observe(viewLifecycleOwner) { result ->
            binding.shimmerView.stopShimmer()
            binding.shimmerView.hide()

            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.datass == null && count == 0) {

                        //binding.noData.show()
                        binding.videosRv.hide()
                        //isLoading = true // Reset loading flag here


                    } else {

                       // binding.noData.hide()
                        binding.videosRv.show()
                        followFlag = result.data.target_user?.target_user_follow_flag.toString()
                        count += result.data.datass?.count() ?: 0

                    }


//                        target_user_follow_flag = result.data.target_user?.target_user_follow_flag.toString()


                    result.data.datass?.forEach { item ->
                        var vidLink = ""
                        if (!(item.vimeo_detials == null)) {
                            if (item != null) {
                                val adaptiveFile =
                                    item.vimeo_detials.files?.firstOrNull { it.rendition == "adaptive" || it.rendition == "360" }
                                vidLink = adaptiveFile?.link ?: item.file

                                //                            Log.e("***", item.vimeo_detials.files.toString())
                                Log.d("AdaptiveLink", item.id)
                                Log.d("hellohelloworld", " item.id : " + item.id)

                                isLoading = false
                                isLastPage = false
                            Log.d("*****2", item.id)
                            newArrVideoModel.add(
                                NewAppendItItems(
                                    item.title,
                                    item.id.toString(),
                                    item.created_at,
                                    vidLink,
                                    item.auther?.uid ?: "",
                                    item.auther?.username ?: "",
                                    item.vimeo_detials?.duration.toString(),

                                    item.vimeo_detials?.pictures?.base_link
                                        ?: "http://kenzalarabnew.br-ws.com.dedivirt1294.your-server.de/storage/images/users/profile_image/1788245666559364.jpg",
                                    //                                firstName = item.auther.profile_data.first_name,
                                    lastName = item.auther?.profile_data?.last_name ?: "",
                                    type = item.auther?.type ?: "",
                                    bandNam = item.auther?.profile_data?.band_name ?: "",
                                    userPic = item.auther?.profile_data?.user_picture ?: "",
                                    favorites = item.video_actions_per_user?.favorites.toString(),
                                    userSave = item.video_actions_per_user?.save.toString(),
                                    target_user = result.data.target_user,
                                    video_counts = item.video_counts,
                                    nodeId = item.id
                                )
                            )
                        }
                            else{
                                isLoading = true
                                isLastPage = true
                            }
                    }
                    }
                    setRecyclerView()

      //              addExtraItems()
//                    videoAdapter.notifyDataSetChanged()
                    binding.progressBar.hide()


                }


                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    binding.progressBar.hide()

                }

                is NetworkResults.NoInternet -> TODO()
            }
        }
    }

    fun addExtraItems(){

        clearExtra()
        Log.d("***2", "count:${newArrVideoModel.count()}")
        if ( newArrVideoModel.count() % 3 != 0){
            var extra = NewAppendItItems("", "", "", "-1", "", "", "", "",
                "", "", "", "", "", "", "", "", null, null,
                0, 0, 0
            )
            if (newArrVideoModel.count() % 3 == 1) {
                newArrVideoModel.add(extra)
                newArrVideoModel.add(extra)
            }
            if (newArrVideoModel.count() % 3 == 2) {
                newArrVideoModel.add(extra)
            }
        }
    }

    fun clearExtra(){
        newArrVideoModel.removeIf { item ->
            item.videoUrl == "-1"
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
            if ( isLastPage == true){

            }else{
                mainViewModel.retriveUserVideos("3", userIdes, "0", currentPage.toString())

            }
        }
    }

    private fun switchToLinearLayout(position: Int) {
        isLinearLayout = true
        videoAdapter.setLinearLayoutMode(true)
        binding.videosRv.layoutManager = LinearLayoutManager(requireContext())
        binding.videosRv.adapter = videoAdapter
        binding.videosRv.scrollToPosition(position)
        binding.constraintLayoutHeader.hide()
        videoAdapter.notifyDataSetChanged()


    }

    private fun switchToGridLayout() {
        isLinearLayout = false
        videoAdapter.setLinearLayoutMode(false)
        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.videosRv.adapter = videoAdapter
        binding.constraintLayoutHeader.show()
        binding.videosRv.scrollToPosition(lastClickedPosition)
        videoAdapter.notifyDataSetChanged()

    }


    private fun updateRecyclerView(newItems: List<NewAppendItItems>) {
        val startPosition = newArrVideoModel.size
        newArrVideoModel.addAll(newItems)
        videoAdapter.notifyItemRangeInserted(startPosition, newItems.size)
        binding.progressBar.hide()
    }

    override fun onResume() {
        super.onResume()
        // Handle back press or similar action
        // For example, listen for a back button press in the toolbar
//        binding.includeTap.menu.setNavigationOnClickListener {
//            if (isLinearLayout) {
//                switchToGridLayout()
//            } else {
//                // Handle regular back action
//                navController.navigateUp()
//            }
//        }

    }

    fun setRecyclerView() {
        binding.videosRv.layoutManager = GridLayoutManager(requireContext(), 3)
        //                    switchToGridLayout()
        videoAdapter = VideoItemAdapter(0, newArrVideoModel, object : VideoClick {
            override fun OnVideoClic(pos: List<NewAppendItItems>, position: Int) {
                //                if (!isLinearLayout) {
                //                    switchToLinearLayout(position)
                //                }
                // else, handle the video click in linear layout


            }

            override fun OnVideoClic(position: Int) {
                val intent = Intent(context, VidInnerPlay::class.java)
                    .apply {
//                                    putExtra("dataList", newArrVideoModel) // Assuming YourDataType is Serializable or Parcelable
                        putExtra("position", position)
                    }

                DataHolder.itemsList = newArrVideoModel
                startActivity(intent)
            }
        }, requireContext())


        binding.videosRv.apply {
            adapter = videoAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addOnScrollListener(this@PartitionChannelFragment.onScrollViewListener)
        }
    }

}