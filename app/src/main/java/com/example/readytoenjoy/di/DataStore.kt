package com.alaturing.incidentify.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val  ADVEN_ID_KEY = "advenId"
private const val USER_TOKEN = "user_token"

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {


    @Singleton
    @Provides
    fun provideAuthenticatedUserDatastore(
        @ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(USER_TOKEN) }

        )
    }

    @Singleton
    @Provides
    fun provideAuthenticatedAdvenDatastore(
        @ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(ADVEN_ID_KEY) }

        )
    }
}