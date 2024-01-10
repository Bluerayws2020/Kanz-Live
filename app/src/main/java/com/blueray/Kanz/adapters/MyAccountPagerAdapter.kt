package com.blueray.Kanz.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.Kanz.ui.fragments.SavedVideoFragment
import com.blueray.Kanz.ui.fragments.VideoListFragment

class MyAccountPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount()= 2

    override fun createFragment(position: Int): Fragment {
        return if(position == 0){
            val data = 10
            val videosFragment = VideoListFragment()
            videosFragment.data = data
            videosFragment
        } else{
            val livesFragment = SavedVideoFragment()
            livesFragment
        }
    }
}