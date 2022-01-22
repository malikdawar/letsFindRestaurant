package com.example.restaurants.data.repository

import androidx.annotation.WorkerThread
import com.example.restaurants.base.BaseRepository
import com.example.restaurants.core.extensions.noNetworkErrorMessage
import com.example.restaurants.core.extensions.somethingWentWrong
import com.example.restaurants.data.DataState
import com.example.restaurants.data.local.InMemoryCache
import com.example.restaurants.data.mapper.PlacesMapper
import com.example.restaurants.data.model.Restaurant
import com.example.restaurants.data.remote.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

/**
 * This is an implementation of [PlacesRepository] to handle communication with [ApiInterface] server.
 * @author Malik Dawar
 */
class PlacesRepositoryImpl @Inject constructor(
    private val apiService: ApiInterface
) : PlacesRepository, BaseRepository() {

    @WorkerThread
    override suspend fun loadPlaces(
        location: LatLng,
        bounds: LatLngBounds
    ): Flow<DataState<List<Restaurant>>> {
        return flow {

            val locationStr = "${location.latitude},${location.longitude}"
            apiService.loadPlaces(location = locationStr).apply {

                val cache = InMemoryCache.get()
                val boundedRestaurant = ArrayList<Restaurant>()
                cache.forEach {
                    if (bounds.contains(LatLng(it.latitude, it.longitude)))
                        boundedRestaurant.add(it)
                }
                emit(DataState.success<List<Restaurant>>(boundedRestaurant))

                onSuccessSuspend {
                    PlacesMapper.mapper(data)?.let {
                        InMemoryCache.add(it)

                        emit(DataState.success(it))
                    }
                }
                // handle the case when the API request gets an error response.
                // e.g. internal server error.
            }.onErrorSuspend {
                emit(DataState.error<List<Restaurant>>(message()))
                // handle the case when the API request gets an exception response.
                // e.g. network connection error.
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(DataState.error<List<Restaurant>>(noNetworkErrorMessage()))
                } else {
                    emit(DataState.error<List<Restaurant>>(somethingWentWrong()))
                }
            }
        }
    }
}