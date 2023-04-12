package com.example.githubuserapplication.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapplication.R
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.databinding.ActivityDetailBinding
import com.example.githubuserapplication.data.remote.response.UserDetailResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.nio.file.Files.delete

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel : DetailViewModel
    private var isFavorite = false
    companion object {
        const val USER = "user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers_fragment,
            R.string.following_fragment,
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        supportActionBar?.title = "Detail Github User"

        @Suppress("DEPRECATION")
        val user = intent.getParcelableExtra<FavoriteUser>(USER)

        val username = user?.username.toString()
        setContentView(binding.root)

        detailViewModel = obtainViewModel(this@DetailActivity)
        detailViewModel.getDetailUser(username)

        detailViewModel.detail.observe(this) { user ->
            setDetailData(user)
        }

        detailViewModel.message.observe(this) { message ->
            toastError(message)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.getUserFavoriteById(user?.id!!).observe(this) { list ->
            isFavorite = list.isNotEmpty()
            if (isFavorite) {
                binding.addFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                binding.addFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.userName = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab,position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()


        binding.addFavorite.setOnClickListener {
            if (isFavorite) {
                detailViewModel.delete(user)
            } else {
                detailViewModel.insert(user)
            }
        }

    }

    private fun setDetailData(consumerSearch: UserDetailResponse) {
        binding.profileName.text = consumerSearch.name
        Glide.with(binding.root.context)
            .load(consumerSearch.avatarUrl)
            .into(binding.profileImage)
        binding.profileUsername.text = consumerSearch.login
        binding.followerCount.text = getString(R.string.count_followers, consumerSearch.followers)
        binding.followingCount.text = getString(R.string.count_following, consumerSearch.following)

    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun toastError(message: String?) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }
}
