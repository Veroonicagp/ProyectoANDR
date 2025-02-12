package com.example.readytoenjoy.di

import android.content.Context
import androidx.room.Room
import com.example.readytoenjoy.core.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Database {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ready-to-enjoy-db"
        ).build()
    }

    @Provides
    fun provideActivityDao(database: AppDatabase) = database.activityDao()

    @Provides
    fun provideAdvenDao(database: AppDatabase) = database.advenDao()

}