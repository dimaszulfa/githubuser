package com.wildan.githubusersubmission3.followTab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wildan.githubusersubmission3.detailUser.DetailActivity

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity)
{

    private var username : String ?= null

    fun setUsername(username: String) {
        this.username = username
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        when (position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString(DetailActivity.USER_DETAIL, this.username)
                bundle.putString(DetailActivity.USER_FOLLOW, "followers")
                fragment.arguments = bundle
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString(DetailActivity.USER_DETAIL, this.username)
                bundle.putString(DetailActivity.USER_FOLLOW, "following")
                fragment.arguments = bundle
            }
        }
        return fragment
    }

}