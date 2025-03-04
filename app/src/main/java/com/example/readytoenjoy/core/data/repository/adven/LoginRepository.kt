package com.example.readytoenjoy.core.data.repository.adven

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.readytoenjoy.core.data.local.user.UserLocal
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
private val JWT_KEY = stringPreferencesKey("jwt")

@Singleton
class LoginRepository @Inject constructor(private val api: ReadyToEnjoyApiService,
                                          @ApplicationContext private val context: Context,private val userLocal: UserLocal
) {
    suspend fun login(identifier: String, password: String): String? {
        val response = api.login(LoginRequest(identifier, password))

        if (response.isSuccessful) {
            val userId = response.body()?.user?.id
            val jwt = response.body()?.jwt

            userId?.let {
                val advenResponse = api.getAdvenByUserId(userId)

                if (advenResponse.isSuccessful && advenResponse.body()?.data?.isNotEmpty() == true) {
                    val advenId = advenResponse.body()?.data?.first()?.id

                    context.dataStore.edit { settings ->
                        settings[ADVEN_ID_KEY] = advenId!!
                    }
                    val user = User(
                        id = userId,
                        name = response.body()?.user?.name ?: "",
                        email = response.body()?.user?.email ?: "",
                        advenId = advenId ?: "",
                        token = jwt
                    )
                    userLocal.saveUser(user)
                }
            }
            jwt?.let {
                context.dataStore.edit { settings ->
                    settings[JWT_KEY] = jwt
                }
            }
            return response.body()?.jwt
        }
        return null
    }
    suspend fun getAdvenId(): String? {
        return context.dataStore.data
            .map { settings -> settings[ADVEN_ID_KEY] }
            .first()

    }

    suspend fun getToken(): String? {
        return context.dataStore.data
            .map { settings -> settings[JWT_KEY] }
            .first()

    }

    suspend fun logout(): Boolean {
        return try {
            context.dataStore.edit { settings ->
                settings.remove(ADVEN_ID_KEY)
                settings.remove(JWT_KEY)
            }
            userLocal.clearUser()

            true
        } catch (e: Exception) {
            false
        }
    }
}

