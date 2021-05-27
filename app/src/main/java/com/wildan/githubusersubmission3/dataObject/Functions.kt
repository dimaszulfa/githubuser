package com.wildan.githubusersubmission3.dataObject

import android.database.Cursor
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.wildan.githubusersubmission3.database.DataUser

object Functions {
    fun setVisibility (tv : View, cond : Boolean) {
        return if (cond) tv.visibility = View.VISIBLE
        else tv.visibility = View.GONE
    }
    fun setToast (msg : String, context : FragmentActivity) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    fun mapCursorToList(favCursor: Cursor?): List<DataUser> {
        val favUser = mutableListOf<DataUser>()
        favCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DataUser.COLOUMN_UID))
                val username = getString(getColumnIndexOrThrow(DataUser.COLOUMN_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DataUser.COLOUMN_AVATAR))
                val favourite = getString(getColumnIndexOrThrow(DataUser.COLOUMN_FAVOURITE))
                favUser.add(DataUser(id, username, avatar, favourite.toBoolean()))
            }
        }
        return favUser
    }
    fun mapCursorToObject(favCursor: Cursor?): DataUser {
        var user = DataUser(null, null, null, false)
        favCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DataUser.COLOUMN_UID))
            val username = getString(getColumnIndexOrThrow(DataUser.COLOUMN_USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DataUser.COLOUMN_AVATAR))
            val favourite = getString(getColumnIndexOrThrow(DataUser.COLOUMN_FAVOURITE))
            user = DataUser(id, username, avatar, favourite.toBoolean())
        }
        return user
    }
}