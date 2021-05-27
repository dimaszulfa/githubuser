package com.wildan.githubusersubmission3.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.wildan.githubusersubmission3.BuildConfig
import com.wildan.githubusersubmission3.dataObject.DataUser
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    val detailUsers = MutableLiveData<DataUser>()
    fun setDetails(user: String) {
        val url = "https://api.github.com/users/$user"
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
                    val responseObject = JSONObject(result)
                    val name = responseObject.getString("name")
                    val username = responseObject.getString("login")
                    val repository = responseObject.getString("public_repos")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("company")
                    val followers = responseObject.getString("followers")
                    val following = responseObject.getString("following")
                    val avatar = responseObject.getString("avatar_url")
                    val id = responseObject.getInt("id")
                    val listItems = DataUser(
                        username, name, location, repository, company, followers, following, avatar, id, false
                    )
                    detailUsers.postValue(listItems)


                } catch (e: Exception) {
                    Log.d("tag", e.message.toString())
                }

            }


            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                if (error != null) {
                    Log.d("error", statusCode.toString() + error.message.toString())
                }
            }
        })
    }
    fun getUsers(): LiveData<DataUser> {
        return detailUsers
    }
}