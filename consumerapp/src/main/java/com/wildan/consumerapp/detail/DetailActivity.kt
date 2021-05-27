package com.wildan.consumerapp.detail

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wildan.consumerapp.dataHelper.Functions.setVisibility
import com.wildan.consumerapp.R
import com.wildan.consumerapp.databinding.ActivityDetailBinding
import com.wildan.consumerapp.followTab.SectionPagerAdapter

class DetailActivity : AppCompatActivity() {

    companion object {
        const val USER_FOLLOW = "user_follow"
        const val USER_DETAIL = "user_detail"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

    private var _binding: ActivityDetailBinding?= null
    private val binding get() = _binding as ActivityDetailBinding
    private var detailViewModel = DetailViewModel()
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var extraUsername : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setVisibility(binding.progressBarDetail, true)
        extraUsername = intent.getStringExtra(USER_DETAIL).toString()
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)
        detailViewModel.setDetails(extraUsername)
        getData()
        setTab()
    }

    private fun getData() {
        var text: String?
        detailViewModel.getUsers().observe(this, {
            if (it != null) {
                Glide.with(this)
                    .load(it.avatar)
                    .placeholder(R.drawable.user_placeholder)
                    .apply(RequestOptions.overrideOf(80, 80))
                    .into(binding.avatar)
                if (it.name != "null" || it.name.isBlank())binding.name.text = it.name
                else setVisibility(binding.name, false)

                if (it.location != "null" || it.location.isBlank()){
                    text = resources.getString(R.string.location) + " : " + it.location
                    binding.location.text = text
                }
                else setVisibility(binding.location, false)

                if (it.repository != "null" || it.repository.isBlank()){
                    text = resources.getString(R.string.repository) + " : " + it.repository
                    binding.repository.text = text
                }
                else setVisibility(binding.repository, false)

                if (it.username != "null" || it.username.isBlank()){
                    text = resources.getString(R.string.username) + " : " + it.username
                    binding.username.text = text
                }
                else setVisibility(binding.username, false)

                if (it.company != "null" || it.company.isBlank()){
                    text = resources.getString(R.string.company) + " : " + it.company
                    binding.company.text = text
                }
                else setVisibility(binding.company, false)

                if (it.followers != "null" || it.followers.isBlank()){
                    text = resources.getString(R.string.followers) + " : " + it.followers
                    binding.followers.text = text
                }
                else setVisibility(binding.followers, false)

                if (it.following != "null" || it.following.isBlank()){
                    text = resources.getString(R.string.following) + " : " + it.following
                    binding.following.text = text
                }
                else setVisibility(binding.following, false)
            }
            setVisibility(binding.progressBarDetail, false)
        })
    }

    private fun setTab() {
        sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.setUsername(extraUsername)
        viewPager2  = binding.viewPager2
        viewPager2.adapter = sectionPagerAdapter
        val tabs : TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}