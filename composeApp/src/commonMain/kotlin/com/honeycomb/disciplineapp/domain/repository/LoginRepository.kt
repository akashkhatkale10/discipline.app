package com.honeycomb.disciplineapp.domain.repository

import com.honeycomb.disciplineapp.data.dto.UserData

interface LoginRepository {

    suspend fun loginUser(user: UserData): Result<UserData>
    suspend fun getCurrentUser(): Result<UserData?>
//    suspend fun logoutUser()
    suspend fun getCurrentUserData(): Result<UserData?>
}