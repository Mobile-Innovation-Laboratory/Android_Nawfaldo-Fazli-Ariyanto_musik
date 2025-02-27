package com.example.tubes_motion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tubes_motion.core.routes.AppRoutes
import com.example.tubes_motion.data.repository.AuthRepository

@Composable
fun ProfileScreen(navController: NavController, authRepository: AuthRepository) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Home")
        }

        Button(
            onClick = {
                authRepository.logout()

                navController.navigate(AppRoutes.Login.route) {
                    popUpTo(AppRoutes.Home.route) { inclusive = true }
                }
            }
        ) {
            Text("Logout")
        }
    }
}
