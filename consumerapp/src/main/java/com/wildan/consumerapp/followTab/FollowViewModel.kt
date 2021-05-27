package com.wildan.consumerapp.followTab

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.wildan.consumerapp.BuildConfig
import com.wildan.consumerapp.dataHelper.FollowUser
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<FollowUser>>()
    private val status = MutableLiveData<String>()

    fun setFollow(user: String, follow : String) {
        val listItems = ArrayList<FollowUser>()
        val url = "https://api.github.com/users/$user/$follow"
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
                    val response = JSONArray(result)
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val list = FollowUser(username, avatar)
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

    fun getFollow(): LiveData<ArrayList<FollowUser>> {
        return listUsers
    }
    fun getStatus() : LiveData<String> {
        return status
    }
}