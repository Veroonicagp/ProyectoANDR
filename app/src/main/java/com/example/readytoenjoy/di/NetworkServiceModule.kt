package com.example.readytoenjoy.di

import com.example.readytoenjoy.core.data.repository.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.repository.activity.DefaultActivityRepository
import com.example.readytoenjoy.core.data.repository.adven.AdvenRepositoryInterface
import com.example.readytoenjoy.core.data.repository.adven.DefaultAdvenRepository
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepository
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.core.data.network.ReadyToEnjoyApiService
import com.example.readytoenjoy.core.data.network.adevn.AdvenNetworkRepository
import com.example.readytoenjoy.core.data.network.adevn.AdvenNetworkRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Module {

    @Binds
    @Singleton
    abstract fun provideDefaultActivityRepository(defaultActivityRepository: DefaultActivityRepository): ActivityRepositoryInterface

    @Binds
    @Singleton
    abstract fun provideActivityRemoteRepository(activityRemoteRepository: ActivityNetworkRepository): ActivityNetworkRepositoryInterface

    @Binds
    @Singleton
    abstract fun providesAdvenRepository(repository: DefaultAdvenRepository): AdvenRepositoryInterface

    @Binds
    @Singleton
    abstract fun providesAdvenNetworkRepository(networkRepository: AdvenNetworkRepository): AdvenNetworkRepositoryInterface

    //falta añadir los local
}

@Module
@InstallIn(SingletonComponent::class)
class NetworkServiceModule {
    companion object {
        const val STRAPI = "https://readytoenjoy2.onrender.com/api/"
    }

    @Provides
    @Singleton
    fun provideNetworkService(): ReadyToEnjoyApiService {
        val client = OkHttpClient.Builder()
            .readTimeout(120,TimeUnit.SECONDS)
            .connectTimeout(120,TimeUnit.SECONDS)
            .writeTimeout(120,TimeUnit.SECONDS)
            // TODO interceptor
            .build()

        val readyToEnjoyUrl = STRAPI
        return Retrofit.Builder()
            .baseUrl(readyToEnjoyUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadyToEnjoyApiService::class.java)
    }
}