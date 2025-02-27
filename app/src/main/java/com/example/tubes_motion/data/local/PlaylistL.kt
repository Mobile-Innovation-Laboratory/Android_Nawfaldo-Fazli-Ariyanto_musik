package com.example.tubes_motion.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "playlists")
data class PlaylistL(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Date? = null,

    @ColumnInfo(name = "createdAt")
    val createdAt: Date? = null,

    @ColumnInfo(name = "synced")
    val synced: Boolean = false
)