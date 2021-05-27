package com.wildan.githubusersubmission3.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.wildan.githubusersubmission3.BuildConfig
import com.wildan.githubusersubmission3.dataObject.SearchUser
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserViewModel : AndroidViewModel(Application()) {
    val listUsers = MutableLiveData<ArrayList<SearchUser>>()
    private val status = MutableLiveData<String>()

    fun setUsers(user: String) {
        val listItems = ArrayList<SearchUser>()
        val url = "https://api.github.com/search/users?q=$user"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token "+ BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d("tag", result)
                try {
                    var list : SearchUser
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        list = SearchUser(username, avatar)
                        listItems.add(list)

                    }
                    listUsers.postValue(listItems)


                } catch (e: Exception) {
                    Log.d("tag", e.message.toString())
                    setStatus(e.message.toString())
                }

            }


            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                if (error != null) {
                    Log.d("error", statusCode.toString() + error.message.toString())
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    setStatus(errorMessage)
                }
            }
        })
    }

    fun setStatus(stat : String) {
        status.postValue(stat)
    }

    fun getUsers(): LiveData<ArrayList<SearchUser>> {
        return listUsers
    }
    fun getStatus() : LiveData<String> {
        return status
    }
}