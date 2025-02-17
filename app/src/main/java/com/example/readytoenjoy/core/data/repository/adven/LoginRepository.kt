package com.example.readytoenjoy.core.data.repository.adven

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.adevn.model.userResponseLR
import com.example.readytoenjoy.core.data.network.adevn.model.LoginRequest
import com.example.readytoenjoy.core.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore(name = "user_prefs")
private val ADVEN_ID_KEY = stringPreferencesKey("advenId")

@Singleton
class LoginRepository @Inject constructor(private val api: ReadyToEnjoyApiService,
                                          @ApplicationContext private val context: Context
) {
    suspend fun login(identifier: String, password: String): String? {
        val response = api.login(LoginRequest(identifier, password))

        if (response.isSuccessful) {
            val userId = response.body()?.user?.id
            println("DEBUG: UserId: $userId") // Log userId

            userId?.let {
                val advenResponse = api.getAdvenByUserId(userId)
                println("DEBUG: AdvenResponse: ${advenResponse.body()}") // Log adven response

                if (advenResponse.isSuccessful && advenResponse.body()?.data?.isNotEmpty() == true) {
                    val advenId = advenResponse.body()?.data?.first()?.id
                    println("DEBUG: AdvenId: $advenId") // Log advenId

                    context.dataStore.edit { settings ->
                        settings[ADVEN_ID_KEY] = advenId!!
                    }
                }
            }
            return response.body()?.jwt
        }
        return null
    }
    suspend fun getAdvenId(): String? {
        return context.dataStore.data
            .map { settings -> settings[ADVEN_ID_KEY] }
            .first()  // Obtener el valor guardado

    }
}

