package com.wildan.consumerapp.followTab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wildan.consumerapp.dataHelper.FollowUser
import com.wildan.consumerapp.R
import com.wildan.consumerapp.databinding.ItemFollowBinding

class FollowListAdapter
    : RecyclerView.Adapter<FollowListAdapter.ViewHolder>() {
    private val listuser = ArrayList<FollowUser>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val adapterBind = ItemFollowBinding.bind(itemView)

        var photo = adapterBind.listUserpic
        var name = adapterBind.listName

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val followUser = listuser[position]

        Glide.with(holder.itemView.context)
            .load(followUser.avatar)
            .placeholder(R.drawable.user_placeholder)
            .apply(RequestOptions.overrideOf(40, 40))
            .into(holder.photo)

        holder.name.text = followUser.username

    }

    override fun getItemCount(): Int {
        return listuser.size
    }

    fun setData(items: ArrayList<FollowUser>) {
        listuser.clear()
        listuser.addAll(items)
        notifyDataSetChanged()
    }
}