package com.example.readytoenjoy.core.data.repository.adven

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.adevn.model.UserRequest
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRequest
import com.example.readytoenjoy.core.data.network.adevn.model.AventureroData
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
                                             private val api: ReadyToEnjoyApiService
) {

    suspend fun register(username:String, email:String, password:String) {
        val userResponse = api.register(
            UserRequest(
            username,email,password)
        )
        // Me he logueado
        if (userResponse.isSuccessful) {
            userResponse.body()!!.jwt
            val user = userResponse.body()
            val userId = user?.user?.id

            if (userId != null) {

                val aventureroRequest = AdvenRequest(
                    AventureroData(
                        name = username,
                        email = email,
                        password = password,
                        userId = userId
                    )
                )

                val aventureroResponse = api.registerAdven(aventureroRequest)

                if (aventureroResponse.isSuccessful) {
                } else {
                    null
                }

            } else {
                null
            }
        }
        // No me he logueado
        else {
            null
        }

    }


}