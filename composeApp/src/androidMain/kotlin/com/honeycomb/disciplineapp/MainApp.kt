package com.honeycomb.disciplineapp

import android.app.Application
import android.content.Context
import com.honeycomb.disciplineapp.data.di.dataModule
import com.honeycomb.disciplineapp.presentation.di.viewModels
import org.koin.core.context.startKoin

class MainApp : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = this

        startKoin {
            modules(
                viewModels + dataModule
            )
        }
    }
}
