package com.example.dandrademusicapp.network

import com.example.dandrademusicapp.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album
}