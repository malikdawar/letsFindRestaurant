package com.example.restaurants.data.usecases

import com.example.restaurants.data.repository.PlacesRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import javax.inject.Inject

/**
 * A use-case to load the places from Square API and to get the data from cache.
 * @author Malik Dawar
 */
class FetchRestaurantsUseCase @Inject constructor(
    private val repository: PlacesRepository
) {

    suspend operator fun invoke(
        location: LatLng,
        bounds: LatLngBounds
    ) = repository.loadPlaces(
        location = location,
        bounds = bounds
    )
}