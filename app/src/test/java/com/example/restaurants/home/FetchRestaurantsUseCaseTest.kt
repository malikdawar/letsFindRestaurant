package com.example.restaurants.home

import com.example.restaurants.data.DataState
import com.example.restaurants.data.RestaurantsDataSource
import com.example.restaurants.data.repository.PlacesRepository
import com.example.restaurants.data.usecases.FetchRestaurantsUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FetchRestaurantsUseCaseTest {

    @MockK
    private lateinit var repository: PlacesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given when the invoke in FetchRestaurantsUseCase is called then it fetch the restaurants`() = runBlocking {
        // Given
        val sut = FetchRestaurantsUseCase(repository)
        val givenRestaurants = RestaurantsDataSource.getRestaurantsList()

        // When
        coEvery { repository.loadPlaces(any(), any()) }
            .returns(flowOf(DataState.success(givenRestaurants)))

        // Invoke
        val restaurantsListFlow = sut.invoke(mockk(), mockk())

        // Then
        MatcherAssert.assertThat(restaurantsListFlow, CoreMatchers.notNullValue())

        val restaurantsListDataState = restaurantsListFlow.first()
        MatcherAssert.assertThat(restaurantsListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            restaurantsListDataState,
            CoreMatchers.instanceOf(DataState.Success::class.java)
        )

        val restaurantsList = (restaurantsListDataState as DataState.Success).data

        MatcherAssert.assertThat(restaurantsList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(restaurantsList.size, `is`(givenRestaurants.size))
        MatcherAssert.assertThat(restaurantsList[0].id, `is`(givenRestaurants[0].id))
        MatcherAssert.assertThat(restaurantsList[0].name, `is`(givenRestaurants[0].name))
        MatcherAssert.assertThat(restaurantsList[0].latitude, `is`(givenRestaurants[0].latitude))
        MatcherAssert.assertThat(restaurantsList[0].longitude, `is`(givenRestaurants[0].longitude))
        MatcherAssert.assertThat(restaurantsList[0].city, `is`(givenRestaurants[0].city))
        MatcherAssert.assertThat(restaurantsList[0].address, `is`(givenRestaurants[0].address))
        MatcherAssert.assertThat(restaurantsList[0].postcode, `is`(givenRestaurants[0].postcode))
    }
}
