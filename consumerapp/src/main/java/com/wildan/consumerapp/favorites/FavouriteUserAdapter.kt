package com.wildan.consumerapp.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wildan.consumerapp.R
import com.wildan.consumerapp.dataHelper.FavUser
import com.wildan.consumerapp.databinding.ItemGithubUserBinding

class FavouriteUserAdapter : RecyclerView.Adapter<FavouriteUserAdapter.ViewHolder>() {
    private var list = mutableListOf<FavUser>()
    private lateinit var clickCallback : ClickCallback

    fun setClickCallback(clickCallback: ClickCallback) {
        this.clickCallback = clickCallback
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemGithubUserBinding.bind(view)

        val username = binding.listName
        val avatar = binding.listUserpic
        val details = binding.btnDetails
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_github_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favUser = list[position]
        Glide.with(holder.itemView.context)
            .load(favUser.avatar)
            .placeholder(R.drawable.user_placeholder)
            .apply(RequestOptions.overrideOf(130, 130))
            .into(holder.avatar)

        holder.username.text = favUser.username

        holder.details.setOnClickListener {

        }
        holder.details.setOnClickListener {
            list[holder.adapterPosition].username?.let { it1 -> clickCallback.onClick(it1) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(items : List<FavUser>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<FavUser> {
        return list
    }

    interface ClickCallback {
        fun onClick(data : String)
    }
}


