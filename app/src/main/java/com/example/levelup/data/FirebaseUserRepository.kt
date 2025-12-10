package com.example.levelup.data

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun saveUserProfile(
        uid: String,
        firstName: String,
        lastName: String,
        level: Int = 1,
        streak: Int = 0,
        onResult: (Boolean, String?) -> Unit
    ) {
        val data = mapOf(
            "uid" to uid,
            "firstName" to firstName,
            "lastName" to lastName,
            "displayName" to "$firstName $lastName",
            "level" to level,
            "streak" to streak
        )

        usersCollection.document(uid)
            .set(data)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    fun loadAllUsersOnce(onResult: (List<LeaderboardUser>, String?) -> Unit) {
        usersCollection.get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.documents.mapNotNull { doc ->
                    val uid = doc.getString("uid") ?: return@mapNotNull null
                    val displayName = doc.getString("displayName") ?: uid
                    val level = doc.getLong("level")?.toInt() ?: 1
                    val streak = doc.getLong("streak")?.toInt() ?: 0
                    LeaderboardUser(uid, displayName, level, streak)
                }
                onResult(users, null)
            }
            .addOnFailureListener { e ->
                onResult(emptyList(), e.message)
            }
    }
}
