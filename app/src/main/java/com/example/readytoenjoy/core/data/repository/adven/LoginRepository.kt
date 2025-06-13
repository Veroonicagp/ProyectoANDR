/**
 * @file LoginRepository.kt
 * @brief Repositorio para gestión de autenticación y sesiones
 * @details Maneja el login, logout y persistencia de datos de sesión
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.data.repository.adven

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.readytoenjoy.core.data.local.user.UserLocal
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.adevn.model.LoginRequest
import com.example.readytoenjoy.core.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** @brief DataStore para preferencias de usuario */
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

/** @brief Clave para almacenar el ID del aventurero */
private val ADVEN_ID_KEY = stringPreferencesKey("advenId")

/** @brief Clave para almacenar el token JWT */
private val JWT_KEY = stringPreferencesKey("jwt")

/**
 * @class LoginRepository
 * @brief Repositorio principal para operaciones de autenticación
 * @details Gestiona el login, logout y persistencia de datos de sesión utilizando
 *          DataStore para almacenamiento local y API para autenticación remota
 * @repository
 * @singleton
 */
@Singleton
class LoginRepository @Inject constructor(
    private val api: ReadyToEnjoyApiService,
    @ApplicationContext private val context: Context,
    private val userLocal: UserLocal
) {

    /**
     * @brief Autentica un usuario en el sistema
     * @details Envía credenciales al servidor, obtiene token JWT y datos del aventurero,
     *          luego almacena toda la información localmente
     * @param identifier Email o nombre de usuario
     * @param password Contraseña del usuario
     * @return String? Token JWT si el login es exitoso, null si falla
     * @throws Exception Si hay problemas de conectividad
     */
    suspend fun login(identifier: String, password: String): String? {
        val response = api.login(LoginRequest(identifier, password))

        if (response.isSuccessful) {
            val userId = response.body()?.user?.id
            val jwt = response.body()?.jwt
            val isAdmin = response.body()?.user?.isAdmin ?: false

            userId?.let {
                val advenResponse = api.getAdvenByUserId(userId)

                if (advenResponse.isSuccessful && advenResponse.body()?.data?.isNotEmpty() == true) {
                    val advenId = advenResponse.body()?.data?.first()?.id

                    // Almacenar ID del aventurero en DataStore
                    context.dataStore.edit { settings ->
                        settings[ADVEN_ID_KEY] = advenId!!
                    }

                    // Crear y almacenar objeto User completo
                    val user = User(
                        id = userId,
                        name = response.body()?.user?.name ?: "",
                        email = response.body()?.user?.email ?: "",
                        advenId = advenId ?: "",
                        token = jwt,
                        isAdmin = isAdmin
                    )
                    userLocal.saveUser(user)
                }
            }

            // Almacenar JWT en DataStore
            jwt?.let {
                context.dataStore.edit { settings ->
                    settings[JWT_KEY] = jwt
                }
            }
            return response.body()?.jwt
        }
        return null
    }

    /**
     * @brief Obtiene el ID del aventurero almacenado
     * @details Recupera el ID del aventurero desde el almacenamiento local
     * @return String? ID del aventurero o null si no existe
     */
    suspend fun getAdvenId(): String? {
        return context.dataStore.data
            .map { settings -> settings[ADVEN_ID_KEY] }
            .first()
    }

    /**
     * @brief Obtiene el token JWT almacenado
     * @details Recupera el token de autenticación desde el almacenamiento local
     * @return String? Token JWT o null si no existe
     */
    suspend fun getToken(): String? {
        return context.dataStore.data
            .map { settings -> settings[JWT_KEY] }
            .first()
    }

    /**
     * @brief Cierra la sesión del usuario
     * @details Elimina todos los datos de sesión del almacenamiento local
     * @return Boolean true si el logout fue exitoso, false en caso contrario
     */
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