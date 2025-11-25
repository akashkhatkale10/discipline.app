package com.honeycomb.disciplineapp.data.repository

import com.honeycomb.disciplineapp.data.dto.UserData
import com.honeycomb.disciplineapp.domain.repository.LoginRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.app
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

class LoginRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
): LoginRepository {
    override suspend fun loginUser(
        user: UserData,
    ): Result<UserData> {
        try {
            val firebaseUser = firestore.collection("users").document(user.userId).get()
            if (firebaseUser.exists) {
                // user exists, get user data
                (firebaseUser.data<UserData>()).let {
                    return Result.success(it)
                }
            } else {
                // user doesn't exists
                firestore
                    .collection("users")
                    .document(user.userId)
                    .set(user)
                return Result.success(user)
            }
        } catch (e: Exception) {
            // check if user exists
            return Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<UserData?> {
        try {
            if (auth.currentUser != null) {
                val firebaseUser = firestore.collection("users").document(auth.currentUser!!.uid).get()
                return if (firebaseUser.exists) {
                    Result.success(
                        UserData(
                            userId = auth.currentUser!!.uid
                        )
                    )
                } else {
                    // network issue
                    Result.success(null)
                }
            } else {
                return Result.success(null)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getCurrentUserData(): Result<UserData?> {
        try {
            if (auth.currentUser != null) {
                val firebaseUser = firestore.collection("users").document(auth.currentUser!!.uid).get()
                return if (firebaseUser.exists) {
                    (firebaseUser.data<UserData>()).let {
                        Result.success(it)
                    }
                } else {
                    // network issue
                    Result.success(null)
                }
            } else {
                // go to login screen
                return Result.success(null)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}