package com.example.githubuserapplication.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = favoriteRepository.getAllFavoriteUser()

}