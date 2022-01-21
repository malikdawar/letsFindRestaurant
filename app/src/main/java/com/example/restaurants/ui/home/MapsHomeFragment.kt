package com.example.restaurants.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.restaurants.base.BaseFragment
import com.example.restaurants.core.extensions.viewBinding
import com.example.restaurants.ui.home.drag.IDragCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.*
import com.example.restaurants.R
import com.example.restaurants.core.extensions.showToastMsg
import com.example.restaurants.core.utils.LocationUtils
import com.example.restaurants.core.utils.MapsUtils
import com.example.restaurants.data.DataState
import com.example.restaurants.data.model.Restaurant
import com.example.restaurants.databinding.FragmentHomeBinding

@RuntimePermissions
@AndroidEntryPoint
class MapsHomeFragment : BaseFragment(R.layout.fragment_home), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    IDragCallback {

    private var googleMap: GoogleMap? = null
    private val locationUtils = LocationUtils.getInstance(requireActivity())
    private val mapsUtils = MapsUtils.getInstance()
    private val viewModel: MapsHomeViewModel by viewModels()

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.root.setDrag(this)
        viewModel.fragCreated = (savedInstanceState != null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initObservations() {
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState -> {
                    progressDialog.show()
                }

                is ContentState -> {
                    progressDialog.dismiss()
                }

                is ErrorState -> {
                    progressDialog.dismiss()
                    showToastMsg(state.message)
                }
            }
        }

        viewModel.restaurantsDataState.observe(viewLifecycleOwner) { restaurants ->
            restaurants?.let {
                renderMarkers(it)
            }
        }
    }

    private fun renderMarkers(venues: List<Restaurant>) {
        val markersToBeDisplayed = ArrayList<Restaurant>()
        val mainList = viewModel.markers.values
        venues.forEach {
            if (!mainList.contains(it))
                markersToBeDisplayed.add(it)
        }

        googleMap?.let {
            if (viewModel.fragCreated && markersToBeDisplayed.isEmpty()) {
                mainList.forEach { restaurant ->
                    mapsUtils.drawMarker(
                        it,
                        location = LatLng(restaurant.latitude, restaurant.longitude),
                        title = restaurant.name
                    )
                }
            } else {
                markersToBeDisplayed.forEach { restaurant ->
                    val marker = mapsUtils.drawMarker(
                        it,
                        location = LatLng(restaurant.latitude, restaurant.longitude),
                        title = restaurant.name
                    )

                    if (marker != null)
                        viewModel.markers[marker] = restaurant
                }
            }
        }
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getCurrentLocation() {
        if (locationUtils.isLocationEnabled()) {
            locationUtils.initOnLocationChangeListener { location ->
                val currentBounds = googleMap?.projection?.visibleRegion?.latLngBounds

                currentBounds?.let {
                    viewModel.getRestaurants(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ), it
                    )
                }
            }
        } else {
            showToastMsg(getString(R.string.message_enable_location))
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap?.setOnMarkerClickListener(this)
        googleMap?.setMinZoomPreference(15f)
        googleMap?.isMyLocationEnabled = true

        getCurrentLocation()
        initObservations()
    }

    override fun onDrag() {
        if (viewModel.fragCreated)
            viewModel.fragCreated = false
        val currentLatLng = googleMap?.cameraPosition?.target
        val currentBounds = googleMap?.projection?.visibleRegion?.latLngBounds
        viewModel.resetRestaurantsDataState()
        if (currentBounds != null && currentLatLng != null)
            viewModel.getRestaurants(currentLatLng, currentBounds)
    }

    override fun onMarkerClick(p0: Marker): Boolean {

        return false
    }
}
