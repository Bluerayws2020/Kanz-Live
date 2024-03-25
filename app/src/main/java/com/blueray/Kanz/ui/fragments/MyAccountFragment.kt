package com.blueray.Kanz.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.ColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.blueray.Kanz.R
import com.blueray.Kanz.adapters.MyAccountPagerAdapter
import com.blueray.Kanz.databinding.FragmentMyAccountBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.model.Item
import com.blueray.Kanz.model.MyVideo
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.ui.activities.FollowingAndFollowersActivity
import com.blueray.Kanz.ui.activities.Profile
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MyAccountFragment : Fragment() {

    private lateinit var binding: FragmentMyAccountBinding
    private val mainViewModel by viewModels<AppViewModel>()
    var userName = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountBinding.inflate(layoutInflater)
        return binding.root
    }



    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPagerWithTapLayout()
        mainViewModel.retriveViewMyProfile()
        getUserProifle()

        binding.settings.setOnClickListener {
          val intent =  Intent(requireContext(),Profile::class.java)
            startActivity(intent)

        }
        binding.logoutBtn.setOnClickListener{

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("تسجيل خروج")
            builder.setMessage("هل انت متاكد من تسجيل الخروج ؟")

            builder.setPositiveButton("نعم") { dialog, _ ->
                val sharedPreferences = requireContext().getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
                sharedPreferences.edit().apply {
                    putString(HelperUtils.UID_KEY, "0")
                    putString(HelperUtils.TOKEN_KEY, "0")
                }.apply()            // go to home activity
                val intent = Intent(requireContext(), com.blueray.Kanz.ui.activities.SplashScreen::class.java)
                startActivity(intent)
                requireActivity().supportFragmentManager.popBackStack()

        }
            builder.setNegativeButton("لا") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
        binding.followingLayout.setOnClickListener {
            val intent  = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
            intent.putExtra("type","myAccount" )
            intent.putExtra("user_id", HelperUtils.getUid(requireContext())) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName",userName ) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("flag","0" )

            startActivity(intent)

        }

        binding.followersLayout.setOnClickListener {
            val intent  = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
            intent.putExtra("type","myAccount" )
            intent.putExtra("user_id", HelperUtils.getUid(requireContext())) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName",userName ) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("flag","1" )

            startActivity(intent)

        }


        binding.followingCount.setOnClickListener {
            val intent  = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
            intent.putExtra("user_id", HelperUtils.getUid(requireContext())) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName",userName ) // Replace 'yourUserId' with the actual user ID

            startActivity(intent)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUpViewPagerWithTapLayout() {
        val adapter = MyAccountPagerAdapter(childFragmentManager, lifecycle)
        val tabListTitle: MutableList<String> = ArrayList()

        val list = listOf("", "")

        for (i in list.indices) {
            val item = list[i]

            // name to the tab layout
            tabListTitle.add(item)
        }

        binding.viewPager.adapter = adapter

        for (i in tabListTitle.indices) {

            binding.tabLayout.addTab(binding.tabLayout.newTab())

        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0) {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.img_1)
                // set drawable bounds
                drawable?.setBounds(
                    0, 0, drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                tab.icon = drawable
            } else {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.img)
                drawable?.setBounds(
                    0, 0, drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                tab.icon = drawable
            }
        }.attach()
    }
    fun getUserProifle(){


        mainViewModel.getMyProfile().observe(viewLifecycleOwner) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    val  data = result.data
                    Glide.with(this).load(result.data.results.profile_image).placeholder(R.drawable.logo2).into(binding.profileImage)


                    binding.followersCount.text =  data.results.followers_count
                    binding.followingCount.text =  data.results.following_count
                    binding.likesCount.text =  data.results.likes_count
                    binding.userName.text = "@"+data.results.user_name
                    userName =  data.results.user_name
                    binding.fullNameTv.text = data.results.first_name + " " + data.results.last_name
                }

                is NetworkResults.Error -> {
                    Log.d("sad error",result.exception.toString())
                }
                is NetworkResults.NoInternet -> TODO()
            }
            }

        }

    override fun onResume() {
        super.onResume()
        mainViewModel.retriveViewMyProfile()
    }

}