/**
 * @file ConnectivityHelper.kt
 * @brief Helper para gestión de conectividad de red
 * @details Proporciona utilidades para verificar el estado de la conexión de red
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @class ConnectivityHelper
 * @brief Helper para verificar y gestionar el estado de conectividad
 * @details Proporciona métodos para verificar la disponibilidad de red de forma
 *          compatible con diferentes versiones de Android. Utiliza ConnectivityManager
 *          y NetworkCapabilities para verificaciones precisas.
 * @singleton
 */
@Singleton
class ConnectivityHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** @brief Manager de conectividad del sistema */
    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * @brief Verifica si hay conexión de red disponible
     * @details Utiliza diferentes métodos según la versión de Android para verificar
     *          la conectividad. Para API 23+ usa NetworkCapabilities, para versiones
     *          anteriores usa NetworkInfo (deprecated pero necesario para compatibilidad).
     * @return Boolean true si hay conexión disponible, false en caso contrario
     */
    fun isNetworkAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkNetworkWithCapabilities()
        } else {
            checkNetworkLegacy()
        }
    }

    /**
     * @brief Verifica conectividad usando NetworkCapabilities (API 23+)
     * @details Método moderno para verificar conectividad usando NetworkCapabilities.
     *          Verifica conexiones WiFi, móviles y Ethernet.
     * @return Boolean true si hay conexión válida, false en caso contrario
     */
    private fun checkNetworkWithCapabilities(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    /**
     * @brief Verifica conectividad usando NetworkInfo (API < 23)
     * @details Método legacy para verificar conectividad en versiones anteriores de Android.
     *          Usa NetworkInfo que está deprecated pero es necesario para compatibilidad.
     * @return Boolean true si hay conexión activa, false en caso contrario
     */
    @Suppress("DEPRECATION")
    private fun checkNetworkLegacy(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    /**
     * @brief Verifica si hay conexión WiFi específicamente
     * @details Determina si la conexión actual es a través de WiFi
     * @return Boolean true si está conectado por WiFi, false en caso contrario
     */
    fun isWifiConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            networkInfo?.isConnected == true
        }
    }

    /**
     * @brief Verifica si hay conexión de datos móviles específicamente
     * @details Determina si la conexión actual es a través de datos móviles
     * @return Boolean true si está conectado por datos móviles, false en caso contrario
     */
    fun isMobileDataConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            networkInfo?.isConnected == true
        }
    }

    /**
     * @brief Obtiene el tipo de conexión actual como string
     * @details Retorna una descripción del tipo de conexión activa
     * @return String Descripción del tipo de conexión ("WiFi", "Móvil", "Ethernet", "Sin conexión")
     */
    fun getConnectionType(): String {
        if (!isNetworkAvailable()) return "Sin conexión"

        return when {
            isWifiConnected() -> "WiFi"
            isMobileDataConnected() -> "Móvil"
            isEthernetConnected() -> "Ethernet"
            else -> "Desconocido"
        }
    }

    /**
     * @brief Verifica si hay conexión Ethernet específicamente
     * @details Determina si la conexión actual es a través de Ethernet
     * @return Boolean true si está conectado por Ethernet, false en caso contrario
     */
    private fun isEthernetConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET)
            networkInfo?.isConnected == true
        }
    }

    /**
     * @brief Verifica si la conexión tiene capacidad de internet
     * @details Verifica específicamente la capacidad NET_CAPABILITY_INTERNET
     * @return Boolean true si tiene capacidad de internet, false en caso contrario
     */
    fun hasInternetCapability(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            isNetworkAvailable()
        }
    }

    /**
     * @brief Verifica si la conexión está validada por el sistema
     * @details Verifica si el sistema ha validado que la conexión tiene acceso real a internet
     * @return Boolean true si la conexión está validada, false en caso contrario
     */
    fun isConnectionValidated(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            isNetworkAvailable()
        }
    }
}