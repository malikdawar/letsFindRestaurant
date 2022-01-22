package com.example.restaurants.data.local

import com.example.restaurants.data.model.Restaurant

object InMemoryCache {
    private val cache = ArrayList<Restaurant>()

    fun add(restaurants: List<Restaurant>) {
        cache.addAll(restaurants)
    }

    fun get(): List<Restaurant> {
        return cache
    }
}
