package com.example.githubuserapplication.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuserapplication.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Update
    fun update(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT  * from favorite WHERE id = :id")
    fun getUserFavoriteById(id: Int): LiveData<List<FavoriteUser>>
}