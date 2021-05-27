package com.wildan.consumerapp.dataHelper

data class DataUser(
        val username: String,
        val name: String,
        val location: String,
        val repository: String,
        val company: String,
        val followers: String,
        val following: String,
        val avatar: String,
        val id : Int ?= null,
        val favourite : Boolean = false
)

data class FavUser(
        val uid: Int?,
        val username: String?,
        val avatar: String?,
        val isFavourite: Boolean
)

data class FollowUser(
        val username: String?,
        val avatar: String?
)