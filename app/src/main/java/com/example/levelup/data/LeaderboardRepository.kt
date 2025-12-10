package com.example.levelup.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class LeaderboardRepository {

    private val db = Firebase.firestore

    fun getLeaderboard() = flow {
        val snapshot = db.collection("users").get().await()
        val users = snapshot.toObjects(LeaderboardUser::class.java)
        emit(users)
    }
}
