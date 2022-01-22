package com.example.restaurants.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Restaurant(
    val id: String?,
    val name: String?,
    val latitude: Double,
    val longitude: Double,
    val city: String?,
    val address: String?,
    val postcode: String?,
) : Parcelable
