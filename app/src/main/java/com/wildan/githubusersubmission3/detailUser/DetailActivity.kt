package com.wildan.githubusersubmission3.detailUser

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wildan.githubusersubmission3.R
import com.wildan.githubusersubmission3.dataObject.Functions
import com.wildan.githubusersubmission3.dataObject.Functions.setToast
import com.wildan.githubusersubmission3.dataObject.Functions.setVisibility
import com.wildan.githubusersubmission3.database.AppDatabase
import com.wildan.githubusersubmission3.database.AppDatabase.Companion.CONTENT_URI
import com.wildan.githubusersubmission3.database.DataUser
import com.wildan.githubusersubmission3.databinding.ActivityDetailBinding
import com.wildan.githubusersubmission3.followTab.SectionPagerAdapter
import com.wildan.githubusersubmission3.viewModel.DetailViewModel
import kotlinx.coroutines.*

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

    private var _binding: ActivityDetailBinding ?= null
    private val binding get() = _binding as ActivityDetailBinding
    private var detailViewModel = DetailViewModel()
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var extraUsername : String
    private lateinit var    dataUser: DataUser
    private var favourite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = AppDatabase.getDatabase(this)
        setVisibility(binding.progressBarDetail, true)
        setVisibility(binding.floatingActionButton, false)
        extraUsername = intent.getStringExtra(USER_DETAIL).toString()
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)
        detailViewModel.setDetails(extraUsername)
        getData(db)
        setTab()

        binding.floatingActionButton.setOnClickListener{
            if (!favourite) {
                CoroutineScope(Dispatchers.Default).launch {
                    val values = ContentValues().apply {
                        put(DataUser.COLOUMN_UID, dataUser.uid)
                        put(DataUser.COLOUMN_USERNAME, dataUser.username)
                        put(DataUser.COLOUMN_AVATAR, dataUser.avatar)
                        put(DataUser.COLOUMN_FAVOURITE, dataUser.favourite)
                    }
                    contentResolver.insert(CONTENT_URI, values)
                    withContext(Dispatchers.Main) {
                        favourite = true
                        binding.floatingActionButton.setImageResource(R.drawable.favourite_clicked)
                        setToast(resources.getString(R.string.add_favorite), this@DetailActivity)
                    }
                }

            }
            else if (favourite) {
                CoroutineScope(Dispatchers.Default).launch {
                    val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataUser.uid)
                    contentResolver.delete(uriWithId, null, null)
                    withContext(Dispatchers.Main) {
                        favourite = false
                        binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                        setToast(resources.getString(R.string.remove_favourite), this@DetailActivity)
                    }
                }
            }
        }

    }



    private fun getData(db: AppDatabase) {
        var text: String?
        detailViewModel.getUsers().observe(this, {
            if (it != null) {
                CoroutineScope(Dispatchers.Default).launch {
                    val data = it.id?.let { it1 -> db.userDao().loadById(it1) }
                    val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + it.id)
                    val cursor = contentResolver.query(uriWithId, null, "WHERE uid =${it.id}", null, null)
                    if (cursor != null && it.id == data?.uid) {
                        dataUser = Functions.mapCursorToObject(cursor)
                        cursor.close()
                        binding.floatingActionButton.setImageResource(R.drawable.favourite_clicked)
                        favourite = true
                    }
                    else {
                        binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                        dataUser = DataUser(it.id, it.username, it.avatar, it.favourite)
                        favourite = false
                    }
                }
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
            setVisibility(binding.floatingActionButton, true)
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