package com.example.readytoenjoy.core.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
    private val advenId = stringPreferencesKey("advenId")

    override suspend fun saveUser(user: User) {
        preferences.edit {
                p ->
            p[idKey] = user.id
            p[emailKey] = user.email
            user.token?.let {
                p[tokenKey] = it
            }
        }
    }

    override suspend fun retrieveUser(): User? {
        val tokenFlow = preferences.data.map { p ->
            p[tokenKey]
        }

        val token = tokenFlow.firstOrNull()
        token?.let {
            return User(
                id = "",
                name = "",
                advenId = "",
                email = "",
                token = token
            )
        }
        return null
    }

    override suspend fun clearUser() {
        preferences.edit {
                p ->
            p[idKey] = ""
            p[advenId] = ""
            p[emailKey] = ""
            p[tokenKey] = ""

        }
    }
}