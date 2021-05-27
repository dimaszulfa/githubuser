package com.wildan.githubusersubmission3.searchUser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wildan.githubusersubmission3.R
import com.wildan.githubusersubmission3.dataObject.Functions.setToast
import com.wildan.githubusersubmission3.dataObject.Functions.setVisibility
import com.wildan.githubusersubmission3.databinding.ActivityMainBinding
import com.wildan.githubusersubmission3.detailUser.DetailActivity
import com.wildan.githubusersubmission3.favourite.FavouriteActivity
import com.wildan.githubusersubmission3.settings.SettingsActivity
import com.wildan.githubusersubmission3.viewModel.UserViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var _binding : ActivityMainBinding
    private val binding get() = _binding
    private lateinit var githubUserAdapter: GithubUserAdapter
    private var userViewModel = UserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setVisibility(binding.tvStatusList, true)
        showRV()

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UserViewModel::class.java)
        userViewModel.getUsers().observe(this, {
            if (it != null) {
                githubUserAdapter.setData(it)
                if (it.isNullOrEmpty()) {
                    setVisibility(binding.tvStatusList, true)
                }
                else setVisibility(binding.tvStatusList, false)
            }
            setVisibility(binding.progressBar, false)
            setVisibility(binding.rvGithubUser, true)

        })

        userViewModel.getStatus().observe(this, {
            if (it != null) setToast(it, this)
            setVisibility(binding.progressBar, false)
        })

        githubUserAdapter.setOnItemClickCallback(object : GithubUserAdapter.OnClick {
            override fun onClicked(data: String) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.USER_DETAIL, data)
                startActivity(intent)
            }
        })

    }

    private fun showRV() {
        setVisibility(binding.progressBar, true)

        githubUserAdapter = GithubUserAdapter()
        githubUserAdapter.notifyDataSetChanged()

        binding.rvGithubUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = githubUserAdapter
        }


        setVisibility(binding.progressBar, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView    = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            this.queryHint = resources.getString(R.string.search_hint)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        setVisibility(binding.rvGithubUser, false)
                        setVisibility(binding.tvStatusList, false)
                        setVisibility(binding.progressBar, true)
                        userViewModel.setUsers(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        Log.d("onQueryTextChange", newText)
                    }
                    if (newText?.isBlank() == false) {
                        setVisibility(binding.progressBar, false)
                        setVisibility(binding.rvGithubUser, false)
                        setVisibility(binding.tvStatusList, false)
                        setVisibility(binding.progressBar, true)
                        userViewModel.setUsers(newText)
                    }
                    return false
                }
            })
        }



        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changeLanguage -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.favourite -> {
                val mIntent = Intent(this, FavouriteActivity::class.java)
                startActivity(mIntent)
            }
            R.id.reminder ->
            {
                val mIntent = Intent(this, SettingsActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}