package com.example.tubes_motion.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Playlist(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val userId: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)