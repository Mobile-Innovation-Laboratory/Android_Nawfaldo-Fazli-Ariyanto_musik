package com.example.tubes_motion.data.mapper

import com.example.tubes_motion.data.local.PlaylistL
import com.example.tubes_motion.data.model.Playlist


fun Playlist.toLocal() = PlaylistL(
    id = this.id,
    title = this.title,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun PlaylistL.toDomain() = Playlist(
    id = this.id,
    title = this.title,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)