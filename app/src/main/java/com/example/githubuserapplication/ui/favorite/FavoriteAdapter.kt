package com.example.githubuserapplication.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.databinding.ItemUsersBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private val listFavoriteUser = ArrayList<FavoriteUser>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setListFavoriteUser(listFavoriteUser: List<FavoriteUser>) {
        val diffCallback = FavoriteDiffCallback(this.listFavoriteUser, listFavoriteUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUser.clear()
        this.listFavoriteUser.addAll(listFavoriteUser)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position])
    }
    override fun getItemCount(): Int {
        return listFavoriteUser.size
    }
    inner class ViewHolder(private val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            with(binding) {
                val (id, username, avatarUrl) = favoriteUser
                Glide.with(itemView.context)
                    .load(avatarUrl)
                    .into(imgItemPhoto)
                tvItemName.text = username
                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(favoriteUser)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }
}