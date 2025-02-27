package com.example.tubes_motion.ui.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tubes_motion.data.repository.AuthRepository
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.tubes_motion.data.model.Playlist
import com.example.tubes_motion.ui.home.widgets.AddPlaylistDialog
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.tubes_motion.core.routes.AppRoutes
import com.example.tubes_motion.data.local.PlaylistDatabase
import com.example.tubes_motion.data.repository.PlaylistRepository
import com.example.tubes_motion.ui.home.widgets.PlaylistItem
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authRepository: AuthRepository,
    context: Context
) {
    var openDialog by remember { mutableStateOf(false) }
    var selectedPlaylist by remember { mutableStateOf(Playlist()) }

    val userId = authRepository.getCurrentUserId() ?: return

    var playlists by remember { mutableStateOf(listOf<Playlist>()) }
    val playlistDao = remember { PlaylistDatabase.getDatabase(context).playlistDao() }
    val playlistRepository = remember { PlaylistRepository(context, userId, playlistDao) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        playlistRepository.getPlaylists().collect { result ->
            result.onSuccess { updatedPlaylists ->
                playlists = updatedPlaylists
            }
        }

        playlistRepository.getPlaylistsRealTime().collect { result ->
            result.onSuccess { updatedPlaylists ->
                playlists = updatedPlaylists
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (openDialog) {
            AddPlaylistDialog (
                playlist = selectedPlaylist,
                onDismiss = {
                    selectedPlaylist = Playlist()
                    openDialog = false
                },
                onSave = { playlist ->
                    scope.launch {
                        val newPlaylist = Playlist(
                            title = playlist.title,
                            userId = userId,
                            createdAt = Date()
                        )

                        playlistRepository.createPlaylist(newPlaylist)
                    }
                    openDialog = false
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.Blue, shape = CircleShape)
                    .clickable {
                        navController.navigate(AppRoutes.Profile.route)
                    }
            ) {
                Text(
                    text = "A",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    openDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Blue,
                    modifier = Modifier.size(34.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playlists) { playlist ->
                PlaylistItem(
                    playlist = playlist,
                    modifier = Modifier.fillMaxWidth(),
                    navController = navController
                )
            }
        }
    }
}