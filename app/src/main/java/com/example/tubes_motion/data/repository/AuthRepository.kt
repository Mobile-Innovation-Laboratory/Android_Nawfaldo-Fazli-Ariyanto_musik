package com.example.tubes_motion.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()

    suspend fun signInWithEmailPassword(email: String, password: String): Result<String> {
        return try {
            val resultAuth = auth.signInWithEmailAndPassword(email, password).await()

            Result.success(resultAuth.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerWithEmailPassword(email: String, password: String): Result<String> {
        return try {
            val resultAuth = auth.createUserWithEmailAndPassword(email, password).await()

            Result.success(resultAuth.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun logout() {
        auth.signOut()
    }
}