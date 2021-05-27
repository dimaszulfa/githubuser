package com.wildan.githubusersubmission3.favourite

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wildan.githubusersubmission3.dataObject.Functions
import com.wildan.githubusersubmission3.database.AppDatabase
import com.wildan.githubusersubmission3.database.AppDatabase.Companion.CONTENT_URI
import com.wildan.githubusersubmission3.database.DataUser
import com.wildan.githubusersubmission3.databinding.ActivityFavouriteBinding
import com.wildan.githubusersubmission3.detailUser.DetailActivity
import kotlinx.coroutines.*

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavouriteBinding
    private lateinit var db : AppDatabase
    private lateinit var favouriteUserAdapter: FavouriteUserAdapter
    private lateinit var favourited : List<DataUser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getDatabase(this)
        showRV()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val favouriteObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavourites()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, favouriteObserver)

        favouriteUserAdapter.setOnItemClickCallback(object : FavouriteUserAdapter.OnClick {
            override fun onClicked(data: String) {
                val intent = Intent(this@FavouriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.USER_DETAIL, data)
                startActivity(intent)
            }
        })
    }

    private fun loadFavourites() {
        CoroutineScope(Dispatchers.Default).launch {
            Functions.setVisibility(binding.progressBar2, false)
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
            favourited = Functions.mapCursorToList(cursor)
            withContext(Dispatchers.Main) {
                favouriteUserAdapter.setData(favourited)
                val data = favouriteUserAdapter.getData()
                if (data.isNullOrEmpty()) Functions.setVisibility(binding.tvEmptyFav, true)
            }
        }
    }

    private fun showRV() {
        Functions.setVisibility(binding.progressBar2, true)
        favouriteUserAdapter = FavouriteUserAdapter()
        favouriteUserAdapter.notifyDataSetChanged()

        binding.rvFavourite.apply {
            layoutManager = LinearLayoutManager(this@FavouriteActivity)
            adapter = favouriteUserAdapter
        }

        loadFavourites()
        Functions.setVisibility(binding.progressBar2, false)
    }
}