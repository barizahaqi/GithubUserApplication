package com.example.githubuserapplication.data.remote.retrofit

import com.example.githubuserapplication.data.remote.response.ItemsItem
import com.example.githubuserapplication.data.remote.response.UserDetailResponse
import com.example.githubuserapplication.data.remote.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") q: String
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    fun getDetailUsers(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowersUsers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingUsers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

}