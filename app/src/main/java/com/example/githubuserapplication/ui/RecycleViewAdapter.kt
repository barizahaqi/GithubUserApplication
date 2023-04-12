package com.example.githubuserapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.databinding.ItemUsersBinding

class RecycleViewAdapter(private val listSearch: ArrayList<FavoriteUser>) : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) : ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val (id, username, avatarUrl) = listSearch[position]
        Glide.with(viewHolder.itemView.context)
            .load(avatarUrl)
            .into(viewHolder.binding.imgItemPhoto)
        viewHolder.binding.tvItemName.text = username
        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSearch[viewHolder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listSearch.size

    class ViewHolder(var binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }
}