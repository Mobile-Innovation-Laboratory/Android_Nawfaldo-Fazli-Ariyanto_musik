package com.example.tubes_motion.data.local

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistL)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistL)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistL)

    @Query("SELECT * FROM playlists")
    fun getPlaylists(): Flow<List<PlaylistL>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: String): PlaylistL?

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: String)

    @Query("SELECT * FROM playlists WHERE synced = 0")
    fun getUnsyncedPlaylists(): Flow<List<PlaylistL>>
}