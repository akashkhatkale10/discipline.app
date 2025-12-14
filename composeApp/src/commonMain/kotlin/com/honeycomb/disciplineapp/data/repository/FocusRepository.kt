package com.honeycomb.disciplineapp.data.repository

import com.honeycomb.disciplineapp.database.FocusSessionEntity
import com.honeycomb.disciplineapp.database.FocusSessionDao
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FocusRepository(
    private val dao: FocusSessionDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    suspend fun saveFocusSession(
        startTimestamp: Long,
        endTimestamp: Long,
        durationSeconds: Int,
        notes: String? = null,
    ) {
        val entity = FocusSessionEntity(
            startTimestamp = startTimestamp,
            endTimestamp = endTimestamp,
            durationSeconds = durationSeconds,
            notes = notes
        )
        dao.insertFocusSession(entity)
        val user = auth.currentUser
        if (user != null) {
            try {
                firestore
                    .collection("users")
                    .document(user.uid)
                    .collection("session")
                    .add(
                        mapOf(
                            "startTimestamp" to startTimestamp,
                            "endTimestamp" to endTimestamp,
                            "durationSeconds" to durationSeconds,
                            "notes" to notes
                        )
                    )
            } catch (e: Exception) {
                // Handle error, e.g., log it
            }
        }
    }
}