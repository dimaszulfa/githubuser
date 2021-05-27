package com.wildan.githubusersubmission3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.wildan.githubusersubmission3.database.AppDatabase
import com.wildan.githubusersubmission3.database.AppDatabase.Companion.AUTHORITY
import com.wildan.githubusersubmission3.database.AppDatabase.Companion.CONTENT_URI
import com.wildan.githubusersubmission3.database.AppDatabase.Companion.TABLE_NAME
import com.wildan.githubusersubmission3.database.DataUser

class FavouriteProvider : ContentProvider() {

    companion object {
        private const val FAVOURITE = 1
        private const val FAVOURITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var db: AppDatabase

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVOURITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVOURITE_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVOURITE_ID) {
            sUriMatcher.match(uri) -> db.userDao().delete(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVOURITE) {
            sUriMatcher.match(uri) -> {
                val data = DataUser(null, null, null, null)
                values?.apply {
                    data.uid = getAsInteger(DataUser.COLOUMN_UID)
                    data.username = getAsString(DataUser.COLOUMN_USERNAME)
                    data.avatar = getAsString(DataUser.COLOUMN_AVATAR)
                    data.favourite = getAsBoolean(DataUser.COLOUMN_FAVOURITE)
                }
                db.userDao().insert(data)
            }
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        db = AppDatabase.getDatabase(context as Context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVOURITE -> db.userDao().getAll()
            FAVOURITE_ID -> uri.lastPathSegment?.let { db.userDao().loadByIds(it.toInt()) }
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return 0
    }
}