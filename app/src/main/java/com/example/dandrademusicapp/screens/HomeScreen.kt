package com.example.dandrademusicapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.dandrademusicapp.components.MiniPlayer
import com.example.dandrademusicapp.model.Album
import com.example.dandrademusicapp.network.RetrofitInstance
import com.example.dandrademusicapp.ui.theme.*

@Composable
fun HomeScreen(onAlbumClick: (Int) -> Unit) {
    var albums by remember { mutableStateOf(listOf<Album>()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var miniPlayerAlbum by remember { mutableStateOf<Album?>(null) }

    LaunchedEffect(true) {
        try {
            albums = RetrofitInstance.api.getAlbums()
            if (albums.isNotEmpty()) miniPlayerAlbum = albums.first()
            isLoading = false
        } catch (e: Exception) {
            error = "Error al cargar álbumes"
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = { MiniPlayer(album = miniPlayerAlbum) }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MusicPurple)
            }
            return@Scaffold
        }

        if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error!!, color = Color.Red)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MusicPurpleLight)
                .padding(padding)
        ) {

            // ── HEADER con degradado ──
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(MusicPurpleDark, MusicPurple)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Text(
                            "Good Morning!",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Text(
                            "Juan Frausto",   // ← pon tu nombre aquí
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                    }
                }
            }

            // ── SECCIÓN Albums (LazyRow horizontal) ──
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Albums", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextOnLight)
                    Text("See more", color = MusicPurple, fontSize = 13.sp)
                }
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(albums) { album ->
                        AlbumCard(
                            album = album,
                            onClick = {
                                miniPlayerAlbum = album
                                onAlbumClick(album.id)
                            }
                        )
                    }
                }
            }

            // ── SECCIÓN Recently Played (LazyColumn dentro de LazyColumn: usamos items) ──
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recently Played", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextOnLight)
                    Text("See more", color = MusicPurple, fontSize = 13.sp)
                }
            }

            items(albums) { album ->
                RecentlyPlayedItem(
                    album = album,
                    onClick = {
                        miniPlayerAlbum = album
                        onAlbumClick(album.id)
                    }
                )
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

// ── Tarjeta grande del carrusel ──
@Composable
fun AlbumCard(album: Album, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = album.image,
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Overlay oscuro
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC000000)),
                        startY = 80f
                    )
                )
        )
        // Texto y botón play
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text(album.title, color = Color.White, fontWeight = FontWeight.Bold,
                fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(album.artist, color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(MusicPurple),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = "Play",
                tint = Color.White, modifier = Modifier.size(20.dp))
        }
    }
}

// ── Ítem de Recently Played ──
@Composable
fun RecentlyPlayedItem(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(album.title, fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp, color = TextOnLight,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${album.artist} • Popular Song",
                    color = TextSubtle, fontSize = 12.sp, maxLines = 1)
            }
            Icon(Icons.Filled.MoreVert, contentDescription = null, tint = TextSubtle)
        }
    }
}