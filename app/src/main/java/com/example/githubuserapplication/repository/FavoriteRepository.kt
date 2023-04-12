package com.example.githubuserapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.data.local.room.FavoriteUserDao
import com.example.githubuserapplication.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val favoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        favoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = favoriteUserDao.getAllFavoriteUser()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.insert(favoriteUser)}
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.delete(favoriteUser) }
    }
    fun update(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.update(favoriteUser) }
    }

    fun getUserFavoriteById(id: Int): LiveData<List<FavoriteUser>> = favoriteUserDao.getUserFavoriteById(id)

}
