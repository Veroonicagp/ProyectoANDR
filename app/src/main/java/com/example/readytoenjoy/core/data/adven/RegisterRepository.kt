package com.example.readytoenjoy.core.data.adven

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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

private val USERNAME_KEY  = stringPreferencesKey("username")
private val EMAIL_KEY  = stringPreferencesKey("email")
private val PASSWORD_KEY  = stringPreferencesKey("password")
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


@Singleton
class RegisterRepository @Inject constructor(@ApplicationContext val context: Context,
                                             private val api: ReadyToEnjoyApiService) {

    suspend fun register(username:String, email:String, password:String): Boolean {
        val response = api.register(AdvenRequest(
            username=username,
            email=email,
            password=password))

        return if(response.isSuccessful){
            context.dataStore.edit {
                    settings ->
                settings[USERNAME_KEY] = username
                settings[EMAIL_KEY] = email
                settings[PASSWORD_KEY] = password
            }
            Log.d("arranca","true")
            true

        } else{
            Log.d("arranca","false")
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