package com.example.restaurants.data

import com.example.restaurants.data.model.*

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


    fun getSquarePlacesModel(): SquarePlacesModel {
        val places = ArrayList<ResultsItem>()
        places.add(
            ResultsItem(
                fsqId = "5352510",
                name = "Burger King",
                distance = 100,
                timezone = "GMT+00:01",
                geocodes = Geocodes(MainLocation(latitude = 35.9235982, longitude = 14.489674)),
                location = Location(
                    locality = "St Julians",
                    address = "57, Burger King, St Julians",
                    postcode = "STJ 0571",
                    country = "malta",
                    region = "Birkirkara",
                    crossStreet = null
                )
            )
        )

        return SquarePlacesModel(places)
    }
}