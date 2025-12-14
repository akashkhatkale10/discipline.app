package com.honeycomb.disciplineapp.data.repository

import com.honeycomb.disciplineapp.data.dto.FocusDto
import com.honeycomb.disciplineapp.data.dto.FocusSessionDto
import com.honeycomb.disciplineapp.data.dto.HomeDto
import com.honeycomb.disciplineapp.database.FocusSessionDao
import com.honeycomb.disciplineapp.database.FocusSessionEntity
import com.honeycomb.disciplineapp.domain.repository.HomeRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    auth: FirebaseAuth,
    private val functions: FirebaseFunctions,
    private val firestore: FirebaseFirestore,
    private val dao: FocusSessionDao,
): HomeRepository, SafeApiCall(auth) {

    override suspend fun getHome(): Result<List<FocusSessionDto>> {
        val cloudResult = safeCall { user ->
            val sessions = firestore
                .collection("users")
                .document(user.uid)
                .collection("session")
                .get()
                .documents
                .map { doc ->
                    doc.data<FocusSessionDto>().copy(id = doc.id)
                }
            Result.success(sessions)
        }
        return if (cloudResult.isSuccess) {
            println("AKASH_LOG:got from cloud: $cloudResult")
            cloudResult
        } else {
            try {
                val local =
                    withContext(Dispatchers.Default) { dao.getAllFocusSessions() }.map { entity ->
                        FocusSessionDto(
                            id = entity.id.toString(),
                            startTimestamp = entity.startTimestamp,
                            endTimestamp = entity.endTimestamp,
                            durationSeconds = entity.durationSeconds,
                            notes = entity.notes
                        )
                    }
                println("AKASH_LOG:got from local: $local")
                Result.success(local)
            } catch (e: Exception) {
                println("AKASH_LOG:failure $e")
                Result.failure(e)
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


