package com.example.readytoenjoy.core.data.adven

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.readytoenjoy.core.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.network.adevn.AdvenRequest
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val NAME_KEY  = stringPreferencesKey("name")
private val SURNAME_KEY  = stringPreferencesKey("surname")
private val EMAIL_KEY  = stringPreferencesKey("email")
private val PASSWORD_KEY  = stringPreferencesKey("password")
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


@Singleton
class RegisterRepository @Inject constructor(@ApplicationContext val context: Context,
                                             private val api: ReadyToEnjoyApiService) {

    suspend fun register(name:String, surname:String, email:String, password:String): Boolean {
        val response = api.register(AdvenRequest(name=name,
            surname=surname,
            email=email,
            password=password))

        return if( response.isSuccessful){
            context.dataStore.edit {
                    settings ->
                settings[NAME_KEY] = name
                settings[SURNAME_KEY] = surname
                settings[EMAIL_KEY] = email
                settings[PASSWORD_KEY] = password
            }
            true
        } else{
            false
        }

    }


    @SuppressLint("SuspiciousIndentation")
    suspend fun isRegistered(): Boolean {
        val localEmail = context.dataStore.data.map {
            it[EMAIL_KEY] ?: ""
        }.first()


        return if (localEmail.isBlank()) {
            false
        }
        else {
            val response = api.readUser(localEmail)
            response.isSuccessful
        }

    }
}