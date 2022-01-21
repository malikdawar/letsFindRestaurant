package com.example.restaurants.data.model

import com.google.gson.annotations.SerializedName

data class SquarePlacesModel(
    val results: List<ResultsItem>?
)

data class ResultsItem(
    @SerializedName("fsq_id")
    val fsqId: String?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("geocodes")
    val geocodes: Geocodes?,
    @SerializedName("location")
    val location: Location?,
)

data class Geocodes(
    @SerializedName("main")
    val main: MainLocation?
)

data class MainLocation(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)

data class Location(
    @SerializedName("country")
    val country: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("locality")
    val locality: String?,
    @SerializedName("postcode")
    val postcode: String?,
    @SerializedName("region")
    val region: String?,
    @SerializedName("cross_street")
    val crossStreet: String?
)




