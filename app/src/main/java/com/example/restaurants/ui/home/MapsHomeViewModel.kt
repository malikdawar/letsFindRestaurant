package com.example.restaurants.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurants.base.BaseViewModel
import com.example.restaurants.data.DataState
import com.example.restaurants.data.model.Restaurant
import com.example.restaurants.data.usecases.FetchRestaurantsUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The MapsHomeViewModel.kt
 * @author Malik Dawar, malikdawar@hotmail.com
 */

@HiltViewModel
class MapsHomeViewModel @Inject constructor(
    private val fetchRestaurantsUseCase: FetchRestaurantsUseCase
) : BaseViewModel() {

    private var _uiState = MutableLiveData<HomeUiState>()
    var uiStateLiveData: LiveData<HomeUiState> = _uiState

    private var _restaurantsDataState: MutableLiveData<List<Restaurant>?> = MutableLiveData()
    val restaurantsDataState: MutableLiveData<List<Restaurant>?>
        get() = _restaurantsDataState

    val markers = HashMap<Marker, Restaurant>()

    var fragCreated: Boolean = false

    fun getRestaurants(location: LatLng, bounds: LatLngBounds) {
        if (_restaurantsDataState.value != null) return

        _uiState.postValue(LoadingState)

        viewModelScope.launch {
            fetchRestaurantsUseCase.invoke(location, bounds).collect { dataState->
                when (dataState) {
                    is DataState.Success -> {
                        _uiState.postValue(ContentState)
                        _restaurantsDataState.postValue(dataState.data)
                    }

                    is DataState.Error -> {
                        _uiState.postValue(ErrorState(dataState.message))
                    }
                }
            }
        }
    }

    fun resetRestaurantsDataState() {
        _restaurantsDataState.value = null
    }
}
