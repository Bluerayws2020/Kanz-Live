package com.blueray.Kanz.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.blueray.Kanz.adapters.FollowersFollowingPagerAdapter
import com.blueray.Kanz.databinding.ActivityFollowingAndFollowersBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.ui.viewModels.AppViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FollowingAndFollowersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFollowingAndFollowersBinding
    private var userId: String? = null
    private var type: String? = null

    private val mainViewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingAndFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("user_id") // Retrieve the user ID
        type = intent.getStringExtra("type")
        val userName = intent.getStringExtra("userName") // Retrieve the user ID
        val flag = intent.getStringExtra("flag")

        if (HelperUtils.getUid(this) == userId) {
            binding.includeTab.title.text = "حسابي"
        } else {
            binding.includeTab.title.text = "@$userName"

        }
        binding.includeTab.back.setOnClickListener {
            onBackPressed()
        }

        if (flag != null) {
            setUpViewPagerWithTapLayout(flag)
        } else {
            setUpViewPagerWithTapLayout("0")
        }
    }

    private fun setUpViewPagerWithTapLayout(flag: String) {

        val adapter = FollowersFollowingPagerAdapter(supportFragmentManager, lifecycle, userId , type)
        val tabListTitle: MutableList<String> = ArrayList()

        val list = listOf("متابعون", "يتابعون")

        for (i in list.indices) {
            val item = list[i]

            // name to the tab layout
            tabListTitle.add(item)
        }

        binding.viewPager.adapter = adapter

        if (flag == "1")
            binding.viewPager.setCurrentItem(0, false)
        else
            binding.viewPager.setCurrentItem(1, false)

        for (item in tabListTitle) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(item))
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabListTitle[position]
        }.attach()
    }


}