package com.example.restaurants.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurants.data.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * The MainViewModel.kt, shared viewModel to transect the data b/w fragments
 * @author Malik Dawar, malikdawar@hotmail.com
 */


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private var _selectedRestaurantModel = MutableLiveData<Restaurant>()
    var selectedRestaurantModel: LiveData<Restaurant> = _selectedRestaurantModel

    fun updateSelectedRestaurant(restaurant: Restaurant) {
        _selectedRestaurantModel.value = restaurant
    }
}
