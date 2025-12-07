package com.honeycomb.disciplineapp.data.repository

import com.honeycomb.disciplineapp.data.dto.FocusDto
import com.honeycomb.disciplineapp.data.dto.HomeDto
import com.honeycomb.disciplineapp.domain.repository.HomeRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.functions.FirebaseFunctions

class HomeRepositoryImpl(
    auth: FirebaseAuth,
    private val functions: FirebaseFunctions,
    private val firestore: FirebaseFirestore,
): HomeRepository, SafeApiCall(auth) {

    override suspend fun getHome(): Result<List<FocusDto>> {
        return safeCall {
            val focus = firestore
                .collection("users")
                .document(it.uid)
                .collection("focus")
                .get()

            if (focus.documents.isNotEmpty()) {
                Result.success(focus.documents.map { focus -> focus.data<FocusDto>() })
            } else {
                Result.success(emptyList())
            }
        }
    }
}



open class SafeApiCall(
    private val auth: FirebaseAuth
) {
    suspend fun <T> safeCall(
        call: suspend (user: FirebaseUser) -> Result<T>
    ): Result<T> {
        try {
            if (auth.currentUser == null) return Result.failure(UnauthorizedException())

            return call(auth.currentUser!!)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}

data class UnauthorizedException(
    override val message: String = "Unauthorized"
): Exception()


