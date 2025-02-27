package com.example.tubes_motion.core.routes

sealed class AppRoutes(val route: String) {
    // Authentication Routes
    object Splash : AppRoutes("splash")
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")

    // Main App Routes
    object Home : AppRoutes("home")
    object PlaylistDetail : AppRoutes("playlistDetail/{playlistId}") {
        fun createRoute(playlistId: String) = "playlistDetail/$playlistId"
    }
    object Profile : AppRoutes("profile")
}