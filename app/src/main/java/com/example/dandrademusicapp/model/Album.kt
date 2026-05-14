package com.example.dandrademusicapp.model

import kotlinx.serialization.Serializable

data class Album(
    val id: Int,
    val title: String,
    val artist: String,
    val description: String,
    val image: String,
    val genre: String? = null
)

@Serializable
data class AlbumNav(
    val id: Int
)