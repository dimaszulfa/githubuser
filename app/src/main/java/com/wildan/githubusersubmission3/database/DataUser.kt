package com.wildan.githubusersubmission3.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AppDatabase.TABLE_NAME)
data class DataUser (
    @PrimaryKey(autoGenerate = false) var uid : Int ?= null,
    @ColumnInfo(name = COLOUMN_USERNAME) var username : String?,
    @ColumnInfo(name = COLOUMN_AVATAR) var avatar : String?,
    @ColumnInfo(name = COLOUMN_FAVOURITE) var favourite : Boolean?
) {
    companion object {
        const val COLOUMN_USERNAME = "username"
        const val COLOUMN_AVATAR = "avatar"
        const val COLOUMN_FAVOURITE = "favourite"
        const val COLOUMN_UID = "uid"
    }
}
