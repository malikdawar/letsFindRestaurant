package com.example.restaurants.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.restaurants.TestCoroutineRule
import com.example.restaurants.data.DataState
import com.example.restaurants.data.RestaurantsDataSource
import com.example.restaurants.data.mapper.PlacesMapper
import com.example.restaurants.data.model.Restaurant
import com.example.restaurants.data.repository.PlacesRepository
import com.example.restaurants.data.usecases.FetchRestaurantsUseCase
import com.example.restaurants.ui.home.ContentState
import com.example.restaurants.ui.home.ErrorState
import com.example.restaurants.ui.home.HomeUiState
import com.example.restaurants.ui.home.MapsHomeViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MapsHomeViewModelTest {

    // Subject under test
    private lateinit var sut: MapsHomeViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = TestCoroutineRule()

    @MockK
    lateinit var fetchRestaurantsUseCase: FetchRestaurantsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = MapsHomeViewModel(fetchRestaurantsUseCase)
    }

    @Test
    fun `when MapsHomeViewModel is initialized and getRestaurants is called then resturants list are fetched`() =
        runBlocking {
            // Given
            val givenRestaurants = RestaurantsDataSource.getRestaurantsList()
            val uiObserver = mockk<Observer<HomeUiState>>(relaxed = true)
            val restaurantsListObserver = mockk<Observer<List<Restaurant>?>>(relaxed = true)

            // When
            coEvery { fetchRestaurantsUseCase.invoke(any(), any()) }
                .returns(flowOf(DataState.success(givenRestaurants)))

            // Invoke
            sut.uiStateLiveData.observeForever(uiObserver)
            sut.restaurantsDataState.observeForever(restaurantsListObserver)

            sut.getRestaurants(mockk(), mockk())

            // Then
            coVerify(exactly = 1) { fetchRestaurantsUseCase.invoke(any(), any()) }
            verify { uiObserver.onChanged(match { it == ContentState }) }
            verify { restaurantsListObserver.onChanged(match { it.size == givenRestaurants.size }) }
        }
}
