package com.wildan.githubusersubmission3.settings

import android.content.Context
import androidx.core.content.edit

internal class SharedPrefs(context: Context) {
    companion object {
        private const val PREFS_NAME = "settings_pref"
        private const val REMINDER = "reminder"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setReminderState(value : Boolean) {
        preferences.edit{
            putBoolean(REMINDER, value)
        }
    }

    fun getReminderState(): Boolean {
        return preferences.getBoolean(REMINDER, false)
    }
}