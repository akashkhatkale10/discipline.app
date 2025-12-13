package com.honeycomb.disciplineapp.di

import com.honeycomb.disciplineapp.data.repository.TimerRepository
import com.honeycomb.disciplineapp.database.AppDatabase
import com.honeycomb.disciplineapp.database.createDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { createDatabase() }
    single { get<AppDatabase>().timerDao() }
    single { TimerRepository(get()) }
}
