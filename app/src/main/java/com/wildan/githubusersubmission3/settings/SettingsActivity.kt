package com.wildan.githubusersubmission3.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wildan.githubusersubmission3.R
import com.wildan.githubusersubmission3.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private var settingsBinding : ActivitySettingsBinding ?= null
    private val binding get() = settingsBinding as ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val reminder = Reminder()
        val sharedPrefs = SharedPrefs(this)

        binding.switchReminder.isChecked = sharedPrefs.getReminderState()
        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !sharedPrefs.getReminderState()) {
                val repeatMessage = resources.getString(R.string.remindermessage)
                reminder.setRepeatingReminder(this, repeatMessage)
                sharedPrefs.setReminderState(true)
            }
            else {
                reminder.cancelReminder(this)
                sharedPrefs.setReminderState(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsBinding = null
    }
}