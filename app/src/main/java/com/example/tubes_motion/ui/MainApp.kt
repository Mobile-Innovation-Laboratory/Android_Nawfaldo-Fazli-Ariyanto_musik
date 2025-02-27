package com.example.tubes_motion.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tubes_motion.core.routes.AppRoutes
import com.example.tubes_motion.data.repository.AuthRepository
import com.example.tubes_motion.ui.home.HomeScreen

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Splash.route
    ) {
        composable(AppRoutes.Splash.route) {
            SplashScreen(navController, authRepository)
        }

        composable(AppRoutes.Login.route) {
            LoginScreen(navController, authRepository)
        }

        composable(AppRoutes.Register.route) {
            RegisterScreen(navController, authRepository)
        }

        composable(AppRoutes.Home.route) {
            HomeScreen(navController, authRepository, context)
        }

        composable(AppRoutes.PlaylistDetail.route) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId") ?: return@composable
            PlaylistDetailScreen(playlistId, authRepository, navController, context)
        }

        composable(AppRoutes.Profile.route) {
            ProfileScreen(navController, authRepository)
        }
    }
}