package com.example.restaurants.data.remote

import com.example.restaurants.core.utils.Const.PLACE_CATEGORY
import com.example.restaurants.data.model.PhotosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(PLACES_SERVICE_API)
    suspend fun loadPlaces(
        @Query("ll") location: String,
        @Query("radius") radius: Int = 100,
        @Query("categories") categories: String = PLACE_CATEGORY
    ): ApiResponse<PhotosResponse>

    companion object {
        const val PLACES_SERVICE_API = "/places/search"
    }
}