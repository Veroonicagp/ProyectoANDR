package com.example.readytoenjoy.di

import com.example.readytoenjoy.core.data.adven.AdvenRepositoryInterface
import com.example.readytoenjoy.core.data.adven.DefaultAdvenRepository
import com.example.readytoenjoy.core.network.adevn.AdvenNetworkRepository
import com.example.readytoenjoy.core.network.adevn.AdvenNetworkRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun providesAdvenRepository(repository:DefaultAdvenRepository):AdvenRepositoryInterface

    @Binds
    @Singleton
    fun providesAdvenNetworkRepository(networkRepository:AdvenNetworkRepository):AdvenNetworkRepositoryInterface
}