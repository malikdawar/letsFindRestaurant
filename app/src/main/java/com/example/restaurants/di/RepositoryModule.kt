package com.example.restaurants.di

import com.example.restaurants.data.remote.ApiInterface
import com.example.restaurants.data.repository.PlacesRepository
import com.example.restaurants.data.repository.PlacesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The Dagger Module for providing repository instances.
 * @author Malik Dawar
 */
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideImagineRepository(apiService: ApiInterface): PlacesRepository {
        return PlacesRepositoryImpl(apiService)
    }
}
