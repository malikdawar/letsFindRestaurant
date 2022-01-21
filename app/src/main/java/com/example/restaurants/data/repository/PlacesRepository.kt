package com.example.restaurants.data.repository

import com.example.restaurants.data.DataState
import com.example.restaurants.data.model.Restaurant
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.Flow

/**
 * PlacesRepository is an interface data layer to handle communication with any data source such as Server or local cache.
 * @see [PlacesRepositoryImpl] for implementation of this class to utilize Square API for nearby places.
 * @author Malik Dawar
 */
interface PlacesRepository {
    suspend fun loadPlaces(
        location: LatLng,
        bounds: LatLngBounds
    ): Flow<DataState<List<Restaurant>>>
}