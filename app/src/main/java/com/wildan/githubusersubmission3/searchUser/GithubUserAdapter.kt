package com.wildan.githubusersubmission3.searchUser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wildan.githubusersubmission3.R
import com.wildan.githubusersubmission3.dataObject.SearchUser
import com.wildan.githubusersubmission3.databinding.ItemGithubUserBinding

class GithubUserAdapter
    : RecyclerView.Adapter<GithubUserAdapter.ViewHolder>() {
    private val listuser = ArrayList<SearchUser>()

    private lateinit var clickCallback: OnClick

    interface OnClick {
        fun onClicked(data : String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnClick) {
        this.clickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

         private val adapterBind = ItemGithubUserBinding.bind(itemView)

         var photo   = adapterBind.listUserpic
         var name    = adapterBind.listName
         var details = adapterBind.btnDetails

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_github_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        
        val githubUser = listuser[position]

        Glide.with(holder.itemView.context)
            .load(githubUser.avatar)
            .placeholder(R.drawable.user_placeholder)
            .apply(RequestOptions.overrideOf(130, 130))
            .into(holder.photo)

        holder.name.text = githubUser.username

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, it.resources.getText(R.string.moreinfo), Toast.LENGTH_SHORT).show()
        }

        holder.details.setOnClickListener {
            clickCallback.onClicked(listuser[holder.adapterPosition].username)
        }
    }

    override fun getItemCount(): Int {
        return listuser.size
    }

    fun setData(items : ArrayList<SearchUser>) {
        listuser.clear()
        listuser.addAll(items)
        notifyDataSetChanged()
    }

}