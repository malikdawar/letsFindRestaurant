package com.example.restaurants.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.restaurants.R
import com.example.restaurants.base.BaseFragment
import com.example.restaurants.databinding.FragmentRestaurantDetailsBinding
import com.example.restaurants.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The RestaurantDetailsFragment.kt
 * @author Malik Dawar, malikdawar@hotmail.com
 */

@AndroidEntryPoint
class RestaurantDetailsFragment : BaseFragment(R.layout.fragment_restaurant_details) {

    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var bindig: FragmentRestaurantDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bindig = FragmentRestaurantDetailsBinding.inflate(inflater, container, false)
        return bindig.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservations()

    }

    private fun initObservations() {
        sharedViewModel.selectedRestaurantModel.observe(viewLifecycleOwner) {
            bindig.tvDetailsName.text = it.name
            bindig.tvDetailsCity.text = it.city
            bindig.tvDetailsPostCode.text = it.postcode
            bindig.tvDetailsAddress.text = it.address
        }
    }
}
