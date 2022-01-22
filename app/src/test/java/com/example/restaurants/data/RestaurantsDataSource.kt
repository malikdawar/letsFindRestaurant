package com.example.restaurants.data

import com.example.restaurants.data.model.Restaurant

object RestaurantsDataSource {

    fun getRestaurantsList(): ArrayList<Restaurant> {
        val restaurants = ArrayList<Restaurant>()
        return restaurants.also {
            it.add(
                Restaurant(
                    id = "5352510",
                    name = "Burger King",
                    latitude = 35.9235982,
                    longitude = 14.489674,
                    city = "St Julians",
                    address = "57, Burger King, St Julians",
                    postcode = "STJ 0571"
                )
            )
        }
    }
}