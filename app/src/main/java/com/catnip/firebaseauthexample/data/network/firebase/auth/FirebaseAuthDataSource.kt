package com.catnip.firebaseauthexample.data.network.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface FirebaseAuthDataSource {
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): FirebaseUser?
    fun doLogOut(): Boolean
    suspend fun doRegister(fullName: String, email: String, password: String): Boolean
    suspend fun doLogin(email: String, password: String): Boolean
}

class FireBaseAuthDataSourceImpl(private val firebaseAuth: FirebaseAuth): FirebaseAuthDataSource{
    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun doLogOut(): Boolean {
        Firebase.auth.signOut()
        return true
    }

    override suspend fun doRegister(fullName: String, email: String, password: String): Boolean {
        val registerResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.updateProfile(
            userProfileChangeRequest {
                displayName = fullName
            }
        )?.await()
        return registerResult.user != null
    }

    override suspend fun doLogin(email: String, password: String): Boolean {
        val loginResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return loginResult.user != null
    }

}