package com.example.readytoenjo

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
@Module
@InstallIn(SingletonComponent::class)
object DataStore {
    //@Singleton
    //@Provides
    //fun provideAuthenticationUserDatastore(
        //@ApplicationContext context: Context):DataStore<Preferences>{
        //return PreferenceDataStoreFactory.create(
        //    productFile={ context.preferencesDataStoreFile(ADVEN_ID_KEY) }
        //)

}

