package com.example.restaurants.home.mapper

import com.example.restaurants.TestCoroutineRule
import com.example.restaurants.data.RestaurantsDataSource
import com.example.restaurants.data.mapper.PlacesMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class RestaurantMapperTest{

    private lateinit var sut: PlacesMapper

    @Before
    fun setUp() {
        sut = PlacesMapper
    }

    @Throws(IOException::class)
    @Test
    fun `given ApiInterface when the loadRestaurants is triggered then it returns the Restaurants`() {
        //given
        val expectedResponse = RestaurantsDataSource.getRestaurantsList()

        //when
        val response = sut.mapper(RestaurantsDataSource.getSquarePlacesModel())

        // Then
        assertThat(response?.get(0)?.id, `is`(expectedResponse[0].id))
        assertThat(response?.get(0)?.name, `is`(expectedResponse[0].name))
        assertThat(response?.get(0)?.latitude, `is`(expectedResponse[0].latitude))
        assertThat(response?.get(0)?.longitude, `is`(expectedResponse[0].longitude))
        assertThat(response?.get(0)?.city, `is`(expectedResponse[0].city))
        assertThat(response?.get(0)?.address, `is`(expectedResponse[0].address))
        assertThat(response?.get(0)?.postcode, `is`(expectedResponse[0].postcode))
    }
}
