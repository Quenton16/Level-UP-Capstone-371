package com.example.levelup.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthRepository(private val userDao: UserDao) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val currentUser = firebaseAuth.currentUser

    val loggedInUserFlow = firebaseAuth.authStateFlow()
        .flatMapLatest { firebaseUser ->
            if (firebaseUser == null) {
                flowOf(null)
            } else {
                userDao.getUserByEmail(firebaseUser.email!!)
            }
        }
        .stateIn(scope, SharingStarted.Eagerly, null)

    fun register(user: User, password: String): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(user.email, password).continueWith {
                scope.launch { userDao.insert(user) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}

fun FirebaseAuth.authStateFlow() = kotlinx.coroutines.flow.callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) }
    addAuthStateListener(listener)
    awaitClose { removeAuthStateListener(listener) }
}
