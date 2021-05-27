package com.wildan.consumerapp.dataHelper

import android.net.Uri

object DatabaseProvider {

    const val COLOUMN_USERNAME = "username"
    const val COLOUMN_AVATAR = "avatar"
    const val COLOUMN_FAVOURITE = "favourite"
    const val COLOUMN_UID = "uid"
    private const val AUTHORITY = "com.wildan.githubusersubmission3"
    private const val SCHEME = "content"
    private const val TABLE_NAME = "favourite_users"

    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()

}