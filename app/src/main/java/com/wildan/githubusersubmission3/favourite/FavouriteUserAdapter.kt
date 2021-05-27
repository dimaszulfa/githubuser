package com.wildan.githubusersubmission3.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wildan.githubusersubmission3.R
import com.wildan.githubusersubmission3.database.DataUser
import com.wildan.githubusersubmission3.databinding.ItemGithubUserBinding

class FavouriteUserAdapter : RecyclerView.Adapter<FavouriteUserAdapter.ViewHolder>() {

    private var listfavourite = mutableListOf<DataUser>()
    private lateinit var clickCallback: OnClick

    interface OnClick {
        fun onClicked(data : String)
    }


    fun setOnItemClickCallback(onItemClickCallback: OnClick) {
        this.clickCallback = onItemClickCallback
    }

    class ViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        private val bind = ItemGithubUserBinding.bind(item)

        var details = bind.btnDetails
        var username = bind.listName
        var photo = bind.listUserpic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_github_user,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favouriteUser = listfavourite[position]
        Glide.with(holder.itemView.context)
            .load(favouriteUser.avatar)
            .placeholder(R.drawable.user_placeholder)
            .apply(RequestOptions.overrideOf(130 ,130))
            .into(holder.photo)

        holder.username.text = favouriteUser.username

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, it.resources.getText(R.string.moreinfo), Toast.LENGTH_SHORT).show()
        }

        holder.details.setOnClickListener {
            listfavourite[holder.adapterPosition].username?.let { it1 -> clickCallback.onClicked(it1) }
        }
    }

    override fun getItemCount(): Int {
        return listfavourite.size
    }
    fun setData(items : List<DataUser>) {
        listfavourite.clear()
        listfavourite.addAll(items)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<DataUser> {
        return listfavourite
    }
}