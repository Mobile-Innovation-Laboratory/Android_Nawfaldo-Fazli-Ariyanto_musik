package com.example.tubes_motion.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.tubes_motion.data.local.PlaylistDatabase
import com.example.tubes_motion.data.repository.AuthRepository
import com.example.tubes_motion.data.repository.PlaylistRepository
//import com.example.tubes_motion.ui.theme.Tubes_motionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            Tubes_motionTheme {
                AppNavigation()
//            }
        }
    }

    override fun onResume() {
        super.onResume()
        val context = this
        val userId = AuthRepository(context).getCurrentUserId() ?: return
        val db = PlaylistDatabase.getDatabase(context).playlistDao()
        val repository = PlaylistRepository(context, userId, db)

        lifecycleScope.launch {
            repository.syncLocalPlaylists()
        }
    }
}