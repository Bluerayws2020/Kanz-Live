package com.blueray.Kanz.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.Kanz.ui.fragments.HomeVidFrag

class HomePagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val videosFragment = HomeVidFrag()

    override fun getItemCount()= 2
    override fun createFragment(position: Int): Fragment {
        return if(position == 0){
            videosFragment
        } else{
            val livesFragment =
                com.blueray.Kanz.videoliveeventsample.view.fragment.LiveEventListFragment()
           videosFragment.videoAdapter = null
            livesFragment


        }



    }







}