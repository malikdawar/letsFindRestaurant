package com.example.restaurants.data.remote

import com.example.restaurants.core.utils.Const.PLACE_CATEGORY
import com.example.restaurants.data.model.SquarePlacesModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(PLACES_SERVICE_API)
    suspend fun loadPlaces(
        @Query("ll") location: String,
        @Query("radius") radius: Int = 1000,
        @Query("categories") categories: String = PLACE_CATEGORY
    ): ApiResponse<SquarePlacesModel>

    companion object {
        const val PLACES_SERVICE_API = "v3/places/search"
    }
}