package com.example.restaurants.data.mapper

import com.example.restaurants.data.model.Restaurant
import com.example.restaurants.data.model.SquarePlacesModel

object PlacesMapper {

    fun mapper(inValue: SquarePlacesModel?): List<Restaurant>? = try {
        inValue?.results?.map {
            Restaurant(
                id = it.fsqId,
                name = it.name,
                latitude = it.geocodes?.main?.latitude ?: 0.0,
                longitude = it.geocodes?.main?.longitude ?: 0.0,
                city = it.location?.locality,
                address = it.location?.address,
                postcode = it.location?.postcode
            )
        }
    } catch (e: Exception) {
        null
    }
}