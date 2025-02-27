package com.example.tubes_motion.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.tubes_motion.core.routes.AppRoutes
import com.example.tubes_motion.data.repository.AuthRepository

@Composable
fun SplashScreen(
    navController: NavController,
    authRepository: AuthRepository
) {
    LaunchedEffect(Unit) {
        if(authRepository.isUserLoggedIn()) {
            navController.navigate(AppRoutes.Home.route) {
                popUpTo(AppRoutes.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(AppRoutes.Login.route) {
                popUpTo(AppRoutes.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tubes Motion",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}