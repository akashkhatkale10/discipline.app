package com.honeycomb.disciplineapp

import com.honeycomb.disciplineapp.data.di.dataModule
import com.honeycomb.disciplineapp.presentation.di.viewModels
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(viewModels + dataModule)
    }
}