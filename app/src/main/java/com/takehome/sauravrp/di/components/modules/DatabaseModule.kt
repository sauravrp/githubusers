package com.takehome.sauravrp.di.components.modules

import android.content.Context
import androidx.room.Room
import com.takehome.sauravrp.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun providesAppDb(applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "employee-db"
        ).build()
    }
}