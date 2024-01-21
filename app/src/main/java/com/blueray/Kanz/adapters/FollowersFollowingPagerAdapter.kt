package com.blueray.Kanz.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.Kanz.ui.fragments.FollowersFollowingFragment

class FollowersFollowingPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle,
                                      private val userId: String?, private val type: String?
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount()= 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowersFollowingFragment()
        fragment.arguments = Bundle().apply {
            putString("tab_position", position.toString())
            putString("user_id", userId)
            putString("type", type)

        }

        return fragment
    }
}