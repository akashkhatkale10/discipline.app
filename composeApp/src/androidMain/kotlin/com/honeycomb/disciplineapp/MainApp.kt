package com.honeycomb.disciplineapp

import android.app.Application
import android.content.Context
import com.honeycomb.disciplineapp.data.di.dataModule
import com.honeycomb.disciplineapp.presentation.di.viewModels
import com.honeycomb.disciplineapp.presentation.focus_app.data_store.BlockedAppsStore
import com.honeycomb.disciplineapp.presentation.focus_app.data_store.BlockedWebsitesStore
import org.koin.core.context.startKoin

class MainApp : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = this

        BlockedAppsStore.init(this)
        BlockedWebsitesStore.init(this)

        startKoin {
            modules(
                viewModels + dataModule
            )
        }
    }
}
