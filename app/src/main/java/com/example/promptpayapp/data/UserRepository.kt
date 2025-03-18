package com.example.promptpayapp.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class UserRepository (
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    // Sign-up function to register a user with the email provider.
    suspend fun signUp(email: String, password: String, firstName: String, balance: Double): ResultClass<Boolean> =
        try{
            auth.createUserWithEmailAndPassword(email, password).await()

            // Add user to firestore
            val user = User(firstName, email, balance)
            saveUserToFirestore(user)

            ResultClass.Success(true)
        } catch (e: Exception) {
            ResultClass.Error(e)
        }

    // Function to login a User
    suspend fun login(email: String, password: String): ResultClass<Boolean> =
        try{
            auth.signInWithEmailAndPassword(email, password).await()
            ResultClass.Success(true)
        } catch (e: Exception) {
            ResultClass.Error(e)
        }

    // Function to sign out a User
    fun signOut() {
        auth.signOut()
    }

    // Function to save a User
    private suspend fun saveUserToFirestore(user: User) {
        // For each user document we will save them in a collection named "users" providing their email as the Id of the document rather than letting Firestore generate random characters
        firestore.collection("users").document(user.email).set(user, SetOptions.merge()).await()
    }

    // Function to get the current user.
    suspend fun getCurrentUser(): ResultClass<User> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val userDocument = firestore.collection("users").document(uid).get().await()
            val user = userDocument.toObject(User::class.java)
            if (user != null) {
                Log.d("user2","$uid")
                ResultClass.Success(user)
            } else {
                ResultClass.Error(Exception("User data not found"))
            }
        } else {
            ResultClass.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        ResultClass.Error(e)
    }
}