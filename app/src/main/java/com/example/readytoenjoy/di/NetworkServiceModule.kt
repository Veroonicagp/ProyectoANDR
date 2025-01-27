package com.example.readytoenjoy.di

import com.example.readytoenjoy.core.data.activity.ActivityRepositoryInterface
import com.example.readytoenjoy.core.data.activity.DefaultActivityRepository
import com.example.readytoenjoy.core.network.activity.ActivityNetworkRepository
import com.example.readytoenjoy.core.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.core.network.ReadyToEnjoyApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Module {

    @Binds
    @Singleton
    abstract fun provideDefaultActivityRepository(defaultActivityRepository: DefaultActivityRepository): ActivityRepositoryInterface

    @Binds
    abstract fun provideActivityRemoteRepository(activityRemoteRepository: ActivityNetworkRepository): ActivityNetworkRepositoryInterface


}

@Module
@InstallIn(SingletonComponent::class)
class NetworkServiceModule {

    @Provides
    @Singleton
    fun provideNetworkService(): ReadyToEnjoyApiService {

        val readyToEnjoyUrl = "https://readytoenjoy2.onrender.com/api/"
        return Retrofit.Builder()
            .baseUrl(readyToEnjoyUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadyToEnjoyApiService::class.java)
    }
}