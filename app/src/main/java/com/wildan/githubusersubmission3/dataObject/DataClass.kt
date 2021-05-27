package com.wildan.githubusersubmission3.dataObject


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

data class SearchUser(
        val username: String,
        val avatar: String,
)




