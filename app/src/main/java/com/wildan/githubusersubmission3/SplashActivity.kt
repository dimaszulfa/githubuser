package com.wildan.githubusersubmission3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.wildan.githubusersubmission3.searchUser.MainActivity
import com.wildan.githubusersubmission3.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var viewSplashBinding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewSplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewSplashBinding.root)

        loading()
    }

    private fun loading() {
        Handler(Looper.getMainLooper()).postDelayed({
            moveActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 3000)

    }

    private fun moveActivity (act: Intent) {
        startActivity(act)
    }

}