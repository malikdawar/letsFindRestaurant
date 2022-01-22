package com.example.restaurants.home.remote

import com.example.restaurants.data.remote.ApiResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class ApiResponseTest {

    @Test
    fun `test data is is not null when got the Success response`() {
        val apiResponse = ApiResponse.create(200..299, Response.success("hello_test"))
        if (apiResponse is ApiResponse.ApiSuccessResponse) {
            MatcherAssert.assertThat(apiResponse.data, CoreMatchers.notNullValue())
            MatcherAssert.assertThat(apiResponse.data, CoreMatchers.`is`("hello_test"))
        }
    }

    @Test
    fun `test message is not null or blank when hving the Exception response`() {
        val exception = Exception("message")
        val apiResponse = ApiResponse.exception<String>(exception)
        MatcherAssert.assertThat(apiResponse.message, CoreMatchers.`is`("message"))
    }
}
