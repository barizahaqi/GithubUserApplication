package com.example.githubuserapplication.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserapplication.ui.follow.FollowFragment

class     SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var userName: String =""

    override fun createFragment(position: Int): Fragment {
        val fragment= FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FollowFragment.ARG_USERNAME, userName)
        }

        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }



}