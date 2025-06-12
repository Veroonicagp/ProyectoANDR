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

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideDefaultActivityRepository(defaultActivityRepository: DefaultActivityRepository): ActivityRepositoryInterface

    @Binds
    @Singleton
    abstract fun provideActivityRemoteRepository(activityRemoteRepository: ActivityNetworkRepository): ActivityNetworkRepositoryInterface

    @Binds
    @Singleton
    abstract fun provideActivityLocalRepository(activityLocalRepository: ActivityLocalRoom): ActivityLocal

    @Binds
    @Singleton
    abstract fun provideAdvenLocalRepository(advenLocalRepository: AdvenLocalRoom): AdvenLocal

    @Binds
    @Singleton
    abstract fun providesAdvenRepository(repository: DefaultAdvenRepository): AdvenRepositoryInterface

    @Binds
    @Singleton
    abstract fun providesAdvenNetworkRepository(networkRepository: AdvenNetworkRepository): AdvenNetworkRepositoryInterface

    @Binds
    @Singleton
    abstract fun bindUserDatasourceLocal(ds: UserLocalDatasource): UserLocal
}

@Module
@InstallIn(SingletonComponent::class)
class NetworkServiceModule {
    companion object {
        const val STRAPI = "https://readytoenjoy2.onrender.com"

        private const val CONNECT_TIMEOUT = 15L
        private const val READ_TIMEOUT = 20L
        private const val WRITE_TIMEOUT = 20L
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    @Provides
    @AuthInterceptorOkHttpClient
    fun provideAuthenticationInterceptor(userLocalDS: UserLocal): Interceptor {
        return AuthenticationInterceptor(userLocalDS)
    }

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

    @Provides
    @Singleton
    fun provideConnectivityHelper(@ApplicationContext context: Context): ConnectivityHelper {
        return ConnectivityHelper(context)
    }
}