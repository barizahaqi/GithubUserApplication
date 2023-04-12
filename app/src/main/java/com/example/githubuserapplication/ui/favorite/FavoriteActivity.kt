package com.example.githubuserapplication.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.databinding.ActivityFavoriteBinding
import com.example.githubuserapplication.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val binding get() = _binding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        adapter = FavoriteAdapter()
        supportActionBar?.title = "Favorite Github User"
        binding?.rvFavorite?.setHasFixedSize(true)
        favoriteViewModel.getAllFavoriteUser().observe(this) { favoriteList ->
            if (favoriteList != null) {
                binding?.rvNoData?.visibility = View.GONE
                adapter.setListFavoriteUser(favoriteList)
                showRecyclerList()
            }
            else {
                binding?.rvNoData?.visibility = View.VISIBLE
            }

        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showRecyclerList() {
        binding?.rvFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.adapter = adapter
        adapter.setOnItemClickCallback(object :
            FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteUser) {
                val intentDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.USER, data)
                startActivity(intentDetail)

            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}