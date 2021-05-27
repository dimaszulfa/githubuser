package com.wildan.consumerapp.favorites

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wildan.consumerapp.dataHelper.DatabaseProvider.CONTENT_URI
import com.wildan.consumerapp.dataHelper.FavUser
import com.wildan.consumerapp.dataHelper.Functions
import com.wildan.consumerapp.databinding.ActivityMainBinding
import com.wildan.consumerapp.detail.DetailActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var favourited : List<FavUser>
    private lateinit var favouriteUserAdapter : FavouriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                showFavourites()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        favouriteUserAdapter.setClickCallback(object : FavouriteUserAdapter.ClickCallback {
            override fun onClick(data: String) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.USER_DETAIL, data)
                startActivity(intent)
            }

        })

    }

    private fun init() {
        favouriteUserAdapter = FavouriteUserAdapter()
        binding.rvFavourites.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = favouriteUserAdapter
        }
        showFavourites()
        Functions.setVisibility(binding.progressBar, false)
    }

    private fun showFavourites() {
        Functions.setVisibility(binding.tvStatus, false)
        Functions.setVisibility(binding.progressBar, true)
        CoroutineScope(Dispatchers.Default).launch {
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
            favourited = Functions.mapCursorToList(cursor)
            withContext(Dispatchers.Main) {
                favouriteUserAdapter.setData(favourited)
                val data = favouriteUserAdapter.getData()
                if (data.isNullOrEmpty()) Functions.setVisibility(binding.tvStatus, true)
            }
        }
    }
}