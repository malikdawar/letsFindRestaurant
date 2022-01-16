package com.example.restaurants.data.remote
import retrofit2.Response

sealed class ApiResponse<out T> {

    data class ApiSuccessResponse<T>(val response: Response<T>) : ApiResponse<T>() {
        val data: T? = response.body()
    }

    sealed class ApiFailureResponse<T> {
        data class Error<T>(val response: Response<T>) : ApiResponse<T>()

        data class Exception<T>(val exception: Throwable) : ApiResponse<T>() {
            val message: String? = exception.localizedMessage
        }
    }

    companion object {

        fun <T> exception(ex: Throwable) = ApiFailureResponse.Exception<T>(ex)

        fun <T> error(response: Response<T>) = ApiFailureResponse.Error<T>(response)

        fun <T> create(
            successCodeRange: IntRange = 200..299,
            response: Response<T>
        ): ApiResponse<T> = try {
            if (response.raw().code in successCodeRange) {
                ApiSuccessResponse(response)
            } else {
                ApiFailureResponse.Error(response)
            }
        } catch (ex: Exception) {
            ApiFailureResponse.Exception(ex)
        }
    }
}