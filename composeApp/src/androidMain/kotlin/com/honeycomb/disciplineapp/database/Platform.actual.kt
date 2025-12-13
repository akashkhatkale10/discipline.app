package com.honeycomb.disciplineapp.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.honeycomb.disciplineapp.MainApp

actual fun createDatabase(): AppDatabase {
    return Room.databaseBuilder(
        MainApp.context,
        AppDatabase::class.java,
        "focus.db"
    )
        .setDriver(
            BundledSQLiteDriver()
        ).build()
}
