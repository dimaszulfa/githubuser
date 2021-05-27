package com.wildan.githubusersubmission3.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM favourite_users")
    fun getAll(): Cursor

    @Query("SELECT * FROM favourite_users WHERE uid =:userIds")
    fun loadByIds(userIds: Int): Cursor

    @Query("SELECT * FROM favourite_users WHERE uid =:userIds")
    fun loadById(userIds: Int): DataUser

    @Insert
    fun insert(user: DataUser) : Long
    @Query("DELETE FROM favourite_users WHERE uid =:id")
    fun delete(id: String): Int

}