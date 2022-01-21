package com.example.restaurants.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurants.base.BaseViewModel
import com.example.restaurants.data.DataState
import com.example.restaurants.data.model.PhotoModel
import com.example.restaurants.data.model.Restaurant
import com.example.restaurants.data.usecases.FetchPhotosUseCase
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsHomeViewModel @Inject constructor(
    private val fetchPhotosUseCase: FetchPhotosUseCase
) : BaseViewModel() {

    private var _uiState = MutableLiveData<HomeUiState>()
    var uiStateLiveData: LiveData<HomeUiState> = _uiState

    private var _restaurantsDataState: MutableLiveData<List<Restaurant>?> = MutableLiveData()
    val restaurantsDataState: MutableLiveData<List<Restaurant>?>
        get() = _restaurantsDataState

    val markers = HashMap<Marker, Restaurant>()

    var fragCreated: Boolean = false

    /*fun getRestaurants(location: Dto) {


        _restaurantsDataState.value = Loading

        getRestaurants.execute(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res -> _restaurantsDataState.value = res }
            .also { compositeDisposable.add(it) }
    }*/


    fun getRestaurants(location: Dto) {
        if (_restaurantsDataState.value != null) return

        _uiState.postValue(LoadingState)

        viewModelScope.launch {
            fetchPhotosUseCase.invoke(pageNum = page).collect { dataState ->
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
