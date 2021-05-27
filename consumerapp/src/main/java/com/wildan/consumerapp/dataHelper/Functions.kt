package com.wildan.consumerapp.dataHelper

import android.database.Cursor
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

object Functions {
    fun setVisibility (tv : View, cond : Boolean) {
        return if (cond) tv.visibility = View.VISIBLE
        else tv.visibility = View.GONE
    }
    fun setToast (msg : String, context : FragmentActivity) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    fun mapCursorToList(favCursor: Cursor?): List<FavUser> {
        val favUser = mutableListOf<FavUser>()
        favCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseProvider.COLOUMN_UID))
                val username = getString(getColumnIndexOrThrow(DatabaseProvider.COLOUMN_USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseProvider.COLOUMN_AVATAR))
                val favourite = getString(getColumnIndexOrThrow(DatabaseProvider.COLOUMN_FAVOURITE))
                favUser.add(FavUser(id, username, avatar, favourite.toBoolean()))
            }
        }
        return favUser
    }
}