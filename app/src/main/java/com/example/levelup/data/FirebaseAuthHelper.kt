package com.example.levelup.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseAuthHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Register user with Firebase Auth, then save profile in Firestore "users" collection.
     */
    fun registerUser(
        user: User,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onResult(false, task.exception?.message ?: "Registration failed.")
                    return@addOnCompleteListener
                }

                val firebaseUser = auth.currentUser
                val uid = firebaseUser?.uid

                if (uid == null) {
                    onResult(false, "Could not get user ID.")
                    return@addOnCompleteListener
                }

                val profileData = mapOf(
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "dateOfBirth" to user.dateOfBirth,
                    "email" to user.email,
                    // stats for leaderboard (start at 0)
                    "totalXp" to 0,
                    "totalStreak" to 0
                )

                firestore.collection("users")
                    .document(uid)
                    .set(profileData)
                    .addOnSuccessListener {
                        onResult(true, null)
                    }
                    .addOnFailureListener { e ->
                        onResult(false, e.message ?: "Failed to save profile.")
                    }
            }
    }


    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message ?: "Login failed.")
                }
            }
    }

    fun getCurrentUserEmail(): String? = auth.currentUser?.email
}
