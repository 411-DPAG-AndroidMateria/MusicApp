package com.example.dandrademusicapp.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("genre") val genre: String? = null
) {
    val imageUrl: String
        get() = "https://wsrv.nl/?url=${image}&w=400&output=jpg"
}