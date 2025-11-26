package com.honeycomb.disciplineapp.data.di

import com.honeycomb.disciplineapp.data.repository.HomeRepositoryImpl
import com.honeycomb.disciplineapp.data.repository.LoginRepositoryImpl
import com.honeycomb.disciplineapp.data.repository.OnboardingRepositoryImpl
import com.honeycomb.disciplineapp.data.repository.StartRoutineRepositoryImpl
import com.honeycomb.disciplineapp.domain.repository.HomeRepository
import com.honeycomb.disciplineapp.domain.repository.LoginRepository
import com.honeycomb.disciplineapp.domain.repository.OnboardingRepository
import com.honeycomb.disciplineapp.domain.repository.StartRoutineRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.app
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.functions.FirebaseFunctions
import dev.gitlive.firebase.functions.functions
import org.koin.dsl.module

val dataModule = module {
    factory <FirebaseFirestore>{ Firebase.firestore(app = Firebase.app, databaseId = "main") }
    factory <FirebaseFunctions>{ Firebase.functions }
    factory <FirebaseAuth>{ Firebase.auth }
    factory <OnboardingRepository> {
        OnboardingRepositoryImpl(get(), get())
    }
    factory <LoginRepository> {
        LoginRepositoryImpl(get(), get())
    }
    factory <HomeRepository> {
        HomeRepositoryImpl(get())
    }
    factory <StartRoutineRepository> {
        StartRoutineRepositoryImpl(get())
    }
}