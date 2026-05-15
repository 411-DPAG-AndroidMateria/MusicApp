package com.example.dandrademusicapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dandrademusicapp.components.MiniPlayer
import com.example.dandrademusicapp.model.Album
import com.example.dandrademusicapp.network.RetrofitInstance
import com.example.dandrademusicapp.ui.theme.*

@Composable
fun DetailScreen(albumId: String, onBack: () -> Unit) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(albumId) {
        try {
            album = RetrofitInstance.api.getAlbumById(albumId)
            isLoading = false
        } catch (e: Exception) {
            error = e.message ?: e.toString()
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = { MiniPlayer(album = album) }
    ) { padding ->
        when {
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MusicPurple)
            }
            error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error!!, color = Color.Red)
            }
            album != null -> DetailContent(album = album!!, onBack = onBack, padding = padding)
        }
    }
}

@Composable
private fun DetailContent(album: Album, onBack: () -> Unit, padding: PaddingValues) {
    val context = LocalContext.current
    val tracks = (1..10).map { i -> "${album.title} • Track $i" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0FC))
            .padding(padding)
    ) {
        // Header con imagen
        item {
            Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(album.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = album.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MusicPurpleDark.copy(alpha = 0.45f),
                                MusicPurple.copy(alpha = 0.85f)
                            )
                        )
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorito", tint = Color.White)
                    }
                }
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                    Text(album.title, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    Text(album.artist, color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier.size(42.dp).clip(CircleShape).background(MusicPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = "Play",
                                tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Box(
                            modifier = Modifier.size(42.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = "Shuffle",
                                tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                    }
                }
            }
        }

        // About this album
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("About this album", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextOnLight)
                    Spacer(Modifier.height(6.dp))
                    Text(album.description, color = TextSubtle, fontSize = 13.sp, lineHeight = 20.sp)
                }
            }
        }

        // Chip artista
        item {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Surface(shape = RoundedCornerShape(20.dp), color = MusicPurpleLight) {
                    Text(
                        "Artist: ${album.artist}",
                        color = MusicPurple,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // 10 canciones
        items(tracks) { trackTitle ->
            TrackItem(imageUrl = album.imageUrl, title = trackTitle, artist = album.artist)
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
fun TrackItem(imageUrl: String, title: String, artist: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                    color = TextOnLight, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(artist, color = TextSubtle, fontSize = 12.sp)
            }
            Icon(Icons.Filled.MoreVert, contentDescription = null, tint = TextSubtle)
        }
    }
}