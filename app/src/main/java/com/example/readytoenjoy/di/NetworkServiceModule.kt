/**
 * @file NetworkServiceModule.kt
 * @brief Módulo de inyección de dependencias para servicios de red
 * @details Configura y proporciona todas las dependencias relacionadas con networking
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.di

import android.content.Context
import com.example.readytoenjoy.core.data.local.activity.ActivityLocal
import com.example.readytoenjoy.core.data.local.activity.ActivityLocalRoom
import com.example.readytoenjoy.core.data.local.adven.AdvenLocal
import com.example.readytoenjoy.core.data.local.adven.AdvenLocalRoom
import com.example.readytoenjoy.core.data.local.user.UserLocal
import com.example.readytoenjoy.core.data.local.user.UserLocalDatasource
import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.activity.DefaultActivityRepository
import com.example.readytoenjoy.core.data.repository.adven.AdvenRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.DefaultAdvenRepository
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepository
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.adevn.AdvenNetworkRepository
import com.example.readytoenjoy.core.data.network.adevn.AdvenNetworkRepositoryInterface
import com.example.readytoenjoy.core.data.network.user.AuthenticationInterceptor
import com.example.readytoenjoy.core.data.repository.user.UserRepository
import com.example.readytoenjoy.core.data.repository.user.UserRepositoryInterface
import com.example.readytoenjoy.core.utils.ConnectivityHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * @class AppModule
 * @brief Módulo principal de inyección de dependencias
 * @details Vincula interfaces con sus implementaciones concretas
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * @brief Proporciona la implementación del repositorio de actividades
     * @param defaultActivityRepository Implementación concreta del repositorio
     * @return ActivityRepositoryInterface Interface del repositorio
     */
    @Binds
    @Singleton
    abstract fun provideDefaultActivityRepository(defaultActivityRepository: DefaultActivityRepository): ActivityRepositoryInterface

    /**
     * @brief Proporciona la implementación del repositorio remoto de actividades
     * @param activityRemoteRepository Implementación concreta del repositorio remoto
     * @return ActivityNetworkRepositoryInterface Interface del repositorio remoto
     */
    @Binds
    @Singleton
    abstract fun provideActivityRemoteRepository(activityRemoteRepository: ActivityNetworkRepository): ActivityNetworkRepositoryInterface

    /**
     * @brief Proporciona la implementación del repositorio local de actividades
     * @param activityLocalRepository Implementación concreta del repositorio local
     * @return ActivityLocal Interface del repositorio local
     */
    @Binds
    @Singleton
    abstract fun provideActivityLocalRepository(activityLocalRepository: ActivityLocalRoom): ActivityLocal

    /**
     * @brief Proporciona la implementación del repositorio local de aventureros
     * @param advenLocalRepository Implementación concreta del repositorio local
     * @return AdvenLocal Interface del repositorio local
     */
    @Binds
    @Singleton
    abstract fun provideAdvenLocalRepository(advenLocalRepository: AdvenLocalRoom): AdvenLocal

    /**
     * @brief Proporciona la implementación del repositorio de aventureros
     * @param repository Implementación concreta del repositorio
     * @return AdvenRepositoryInterface Interface del repositorio
     */
    @Binds
    @Singleton
    abstract fun providesAdvenRepository(repository: DefaultAdvenRepository): AdvenRepositoryInterface

    /**
     * @brief Proporciona la implementación del repositorio remoto de aventureros
     * @param networkRepository Implementación concreta del repositorio remoto
     * @return AdvenNetworkRepositoryInterface Interface del repositorio remoto
     */
    @Binds
    @Singleton
    abstract fun providesAdvenNetworkRepository(networkRepository: AdvenNetworkRepository): AdvenNetworkRepositoryInterface

    /**
     * @brief Proporciona la implementación del datasource local de usuarios
     * @param ds Implementación concreta del datasource
     * @return UserLocal Interface del datasource local
     */
    @Binds
    @Singleton
    abstract fun bindUserDatasourceLocal(ds: UserLocalDatasource): UserLocal

    /**
     * @brief Proporciona la implementación del repositorio de usuarios
     * @param userRepository Implementación concreta del repositorio
     * @return UserRepositoryInterface Interface del repositorio
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepository: UserRepository): UserRepositoryInterface
}

/**
 * @class NetworkServiceModule
 * @brief Módulo de configuración de servicios de red
 * @details Configura Retrofit, OkHttpClient y servicios relacionados
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkServiceModule {

    companion object {
        /** @brief URL base del servidor Strapi */
        const val STRAPI = "https://readytoenjoy2.onrender.com"

        /** @brief Timeout de conexión en segundos */
        private const val CONNECT_TIMEOUT = 15L

        /** @brief Timeout de lectura en segundos */
        private const val READ_TIMEOUT = 20L

        /** @brief Timeout de escritura en segundos */
        private const val WRITE_TIMEOUT = 20L
    }

    /**
     * @annotation AuthInterceptorOkHttpClient
     * @brief Qualifier para el interceptor de autenticación
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    /**
     * @brief Proporciona el interceptor de autenticación
     * @param userLocalDS Datasource local de usuarios para obtener tokens
     * @return Interceptor Interceptor configurado para autenticación
     */
    @Provides
    @AuthInterceptorOkHttpClient
    fun provideAuthenticationInterceptor(userLocalDS: UserLocal): Interceptor {
        return AuthenticationInterceptor(userLocalDS)
    }

    /**
     * @brief Proporciona cliente HTTP configurado
     * @details Configura OkHttpClient con interceptores, timeouts y logging
     * @param interceptor Interceptor de autenticación
     * @return OkHttpClient Cliente HTTP configurado
     */
    @Provides
    @Singleton
    fun provideHttpClient(@AuthInterceptorOkHttpClient interceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient()
            .newBuilder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * @brief Proporciona el servicio de API configurado
     * @details Configura Retrofit con el cliente HTTP y convertidores
     * @param client Cliente HTTP configurado
     * @return ReadyToEnjoyApiService Servicio de API listo para usar
     */
    @Provides
    @Singleton
    fun provideNetworkService(client: OkHttpClient): ReadyToEnjoyApiService {
        val readyToEnjoyUrl = "https://readytoenjoy2.onrender.com/api/"
        return Retrofit.Builder()
            .baseUrl(readyToEnjoyUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadyToEnjoyApiService::class.java)
    }

    /**
     * @brief Proporciona helper de conectividad
     * @param context Contexto de la aplicación
     * @return ConnectivityHelper Helper para verificar estado de red
     */
    @Provides
    @Singleton
    fun provideConnectivityHelper(@ApplicationContext context: Context): ConnectivityHelper {
        return ConnectivityHelper(context)
    }
}