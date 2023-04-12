package com.example.githubuserapplication.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.data.remote.response.UserDetailResponse
import com.example.githubuserapplication.data.remote.retrofit.ApiConfig
import com.example.githubuserapplication.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _detail = MutableLiveData<UserDetailResponse>()
    val detail: LiveData<UserDetailResponse> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    companion object{
        private const val TAG = "DetailViewModel"
    }

    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getUserFavoriteById(id: Int) : LiveData<List<FavoriteUser>> = favoriteRepository.getUserFavoriteById(id)


    fun insert(favoriteUser: FavoriteUser) {
        favoriteRepository.insert(favoriteUser)
    }
    fun delete(favoriteUser: FavoriteUser) {
        favoriteRepository.delete(favoriteUser)
    }

    fun getDetailUser(input: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUsers(input)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _message.value = response.message()
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _message.value = t.message
            }
        })
    }
}