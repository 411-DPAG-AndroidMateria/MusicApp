package com.example.dandrademusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dandrademusicapp.screens.DetailScreen
import com.example.dandrademusicapp.screens.HomeScreen
import com.example.dandrademusicapp.ui.theme.DAndradeMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DAndradeMusicAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(onAlbumClick = { id ->
                            navController.navigate("detail/$id")
                        })
                    }
                    composable("detail/{id}") { backStack ->
                        val id = backStack.arguments?.getString("id")?.toInt() ?: 0
                        DetailScreen(albumId = id, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}