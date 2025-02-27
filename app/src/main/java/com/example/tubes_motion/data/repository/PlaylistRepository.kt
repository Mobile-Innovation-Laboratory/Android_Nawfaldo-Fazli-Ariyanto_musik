package com.example.tubes_motion.data.repository

import android.content.Context
import com.example.tubes_motion.data.local.PlaylistDao
import com.example.tubes_motion.data.mapper.toDomain
import com.example.tubes_motion.data.mapper.toLocal
import com.example.tubes_motion.data.model.Playlist
import com.example.tubes_motion.helper.isOnline
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistRepository(
    private val context: Context,
    private val userId: String,
    private val playlistDao: PlaylistDao,
) {
    val firestore = FirebaseFirestore.getInstance()
    val playlistRef = firestore.collection("playlist")

    suspend fun syncLocalPlaylists() {
        withContext(Dispatchers.IO) {
            if (isOnline(context)) {
                val localPlaylists = playlistDao.getUnsyncedPlaylists().first()
                for (playlist in localPlaylists) {
                    createPlaylist(playlist.toDomain())
                }
            }
        }
    }

    suspend fun createPlaylist(playlist: Playlist): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val newPlaylist = playlist.copy(userId = userId)
                val id = if (isOnline(context)) {
                    val result = playlistRef.add(newPlaylist).await()
                    result.id
                } else {
                    playlistDao.insertPlaylist(newPlaylist.toLocal())
                    "Saved locally"
                }
                playlistDao.insertPlaylist(newPlaylist.toLocal()) // Always store in Room
                Result.success(id)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun getPlaylists(): Flow<Result<List<Playlist>>> = flow {
        try {
            val playlists = if (isOnline(context)) {
                val snapshot = playlistRef
                    .whereEqualTo("userId", userId)
                    .orderBy("createdAt")
                    .get().await()
                snapshot.documents.mapNotNull { it.toObject<Playlist>()?.copy(id = it.id) }
            } else {
                playlistDao.getPlaylists().first().map { it.toDomain() }
            }
            emit(Result.success(playlists))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getPlaylistsRealTime(): Flow<Result<List<Playlist>>> = callbackFlow {
        val listenerRegistration: ListenerRegistration = playlistRef
            .whereEqualTo("userId", userId)
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(Result.failure(e))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val playlists = snapshot.documents.mapNotNull {
                        it.toObject<Playlist>()?.copy(id = it.id)
                    }
                    launch {
                        playlists.forEach { playlistDao.insertPlaylist(it.toLocal()) } // Keep Room updated
                    }
                    trySend(Result.success(playlists))
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    suspend fun getPlaylistWithId(playlistId: String): Result<Playlist> {
        return try {
            val playlist = if (isOnline(context)) {
                val document = playlistRef.document(playlistId).get().await()
                document.toObject<Playlist>()?.copy(id = document.id)
            } else {
                playlistDao.getPlaylistById(playlistId)?.toDomain()
            }
            if (playlist != null) Result.success(playlist) else Result.failure(Exception("Playlist not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePlaylist(playlistId: String, updatedPlaylist: Playlist): Result<Unit> {
        return try {
            if (isOnline(context)) {
                playlistRef.document(playlistId).set(updatedPlaylist.copy(id = playlistId)).await()
            }
            playlistDao.updatePlaylist(updatedPlaylist.toLocal()) // Always update Room
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePlaylist(playlistId: String): Result<Unit> {
        return try {
            if (isOnline(context)) {
                playlistRef.document(playlistId).delete().await()
            }
            playlistDao.deletePlaylistById(playlistId) // Always delete from Room
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}