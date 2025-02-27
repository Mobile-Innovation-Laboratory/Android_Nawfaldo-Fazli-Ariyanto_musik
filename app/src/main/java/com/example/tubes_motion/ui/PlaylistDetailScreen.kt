package com.example.tubes_motion.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tubes_motion.data.repository.AuthRepository
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.tubes_motion.data.local.PlaylistDatabase
import com.example.tubes_motion.data.model.Playlist
import com.example.tubes_motion.data.repository.PlaylistRepository
import com.example.tubes_motion.ui.home.widgets.AddPlaylistDialog
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    playlistId: String,
    authRepository: AuthRepository,
    navController: NavController,
    context: Context
) {
    val userId = authRepository.getCurrentUserId() ?: return
    val playlistDao = remember { PlaylistDatabase.getDatabase(context).playlistDao() }
    val playlistRepository = remember { PlaylistRepository(context, userId, playlistDao) }
    var playlist by remember { mutableStateOf<Playlist?>(null) }
    var openDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(playlistId) {
        val result = playlistRepository.getPlaylistWithId(playlistId)
        result.onSuccess { playlistData -> playlist = playlistData }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = playlist?.title ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { openDialog = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            playlistRepository.deletePlaylist(playlistId)
                            navController.popBackStack()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            playlist?.let {
                Text(text = "Title: ${it.title}", style = MaterialTheme.typography.bodyLarge)
            } ?: Text("Loading...", style = MaterialTheme.typography.bodyLarge)
        }
    }

    if (openDialog && playlist != null) {
        AddPlaylistDialog(
            playlist = playlist!!,
            onDismiss = { openDialog = false },
            onSave = { updatedPlaylist ->
                coroutineScope.launch {
                    playlistRepository.updatePlaylist(playlistId, updatedPlaylist)
                    playlist = updatedPlaylist
                    openDialog = false
                }
            }
        )
    }
}