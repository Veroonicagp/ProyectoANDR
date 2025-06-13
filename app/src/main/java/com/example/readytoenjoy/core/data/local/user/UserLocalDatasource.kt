package com.example.readytoenjoy.core.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.readytoenjoy.core.model.User
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDatasource @Inject constructor(
    private val preferences: DataStore<Preferences>
): UserLocal{

    private val tokenKey = stringPreferencesKey("token")
    private val emailKey = stringPreferencesKey("email")
    private val idKey = stringPreferencesKey("id")
    private val nameKey = stringPreferencesKey("name")
    private val advenIdKey = stringPreferencesKey("advenId")
    private val isAdminKey = booleanPreferencesKey("isAdmin")

    override suspend fun saveUser(user: User) {
        preferences.edit { p ->
            p[idKey] = user.id
            p[nameKey] = user.name
            p[emailKey] = user.email
            p[advenIdKey] = user.advenId
            p[isAdminKey] = user.isAdmin
            user.token?.let {
                p[tokenKey] = it
            }
        }
    }

    override suspend fun getUser(): User? {
        return try {
            val userFlow = preferences.data.map { p ->
                val token = p[tokenKey]
                val id = p[idKey]
                val name = p[nameKey]
                val email = p[emailKey]
                val advenIdValue = p[advenIdKey]
                val isAdmin = p[isAdminKey] ?: false

                if (token != null && id != null && name != null && email != null && advenIdValue != null) {
                    User(
                        id = id,
                        name = name,
                        advenId = advenIdValue,
                        email = email,
                        token = token,
                        isAdmin = isAdmin
                    )
                } else {
                    null
                }
            }
            userFlow.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun clearUser() {
        preferences.edit { p ->
            p.clear()
        }
    }
}