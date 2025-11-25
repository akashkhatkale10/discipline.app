package com.honeycomb.disciplineapp

import android.app.Application
import com.honeycomb.disciplineapp.data.di.dataModule
import com.honeycomb.disciplineapp.presentation.di.viewModels
import org.koin.core.context.startKoin

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                viewModels + dataModule
            )
        }
    }
}
