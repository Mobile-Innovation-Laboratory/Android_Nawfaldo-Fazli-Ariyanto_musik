package com.example.tubes_motion.ui.home.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tubes_motion.data.model.Playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaylistDialog(
    modifier: Modifier = Modifier,
    playlist: Playlist = Playlist(),
    onDismiss: () -> Unit,
    onSave: (Playlist) -> Unit,
) {
    var playlistTitle by remember { mutableStateOf(playlist.title ?: "") }

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        content = {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Playlist")

                    TextField(
                        value = playlistTitle,
                        onValueChange = {
                            playlistTitle = it
                        },
                        placeholder = {
                            Text(text = "Title")
                        }
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(onClick = {
                            onSave(
                                playlist.copy(
                                    title = playlistTitle,
                                )
                            )
                            onDismiss()
                        }) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    )
}