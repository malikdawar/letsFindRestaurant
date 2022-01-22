package com.example.restaurants.home.remote

import com.example.restaurants.ApiAbstract
import com.example.restaurants.StringGenerator
import com.example.restaurants.TestCoroutineRule
import com.example.restaurants.data.remote.ApiInterface
import com.example.restaurants.data.remote.ApiResponse
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ApiServiceTest : ApiAbstract<ApiInterface>() {

    private lateinit var apiService: ApiInterface

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        apiService = createService(ApiInterface::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun `given ApiInterface when the loadRestaurants is triggered then it returns the Restaurants`() =
        runBlocking {
            // Given
            enqueueResponse("places_response_200.json")

            // Invoke
            val response = apiService.loadPlaces(LatLng(35.45166, 14.17541).toString())
            val responseBody =
                requireNotNull((response as ApiResponse.ApiSuccessResponse).data).results
            mockWebServer.takeRequest()

            // Then
            assertThat(responseBody?.get(0)?.fsqId, `is`("4e6b30f8fa76cd0f77063d20"))
            assertThat(responseBody?.get(0)?.name, `is`("Andrew's Snack Bar"))
            assertThat(responseBody?.get(0)?.geocodes?.main?.latitude, `is`(35.89466491621463))
            assertThat(responseBody?.get(0)?.geocodes?.main?.longitude, `is`(14.469289183616638))
            assertThat(responseBody?.get(0)?.location?.locality, `is`("Birkirkara"))
            assertThat(responseBody?.get(0)?.location?.address, `is`("15, Triq Sant' Antnin"))
            assertThat(responseBody?.get(0)?.location?.postcode, `is`("BKR4901"))
        }
}
