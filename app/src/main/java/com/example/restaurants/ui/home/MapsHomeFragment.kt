package com.example.restaurants.ui.home

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.restaurants.R
import com.example.restaurants.base.BaseFragment
import com.example.restaurants.core.extensions.showToastMsg
import com.example.restaurants.core.extensions.viewBinding
import com.example.restaurants.core.utils.LocationPermissions
import com.example.restaurants.core.utils.LocationUtils
import com.example.restaurants.core.utils.MapsUtils
import com.example.restaurants.data.model.Restaurant
import com.example.restaurants.databinding.FragmentHomeBinding
import com.example.restaurants.ui.MainViewModel
import com.example.restaurants.ui.home.drag.IDragCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsHomeFragment : BaseFragment(R.layout.fragment_home), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    IDragCallback {

    private var googleMap: GoogleMap? = null
    private var locationUtils: LocationUtils? = null
    private val mapsUtils = MapsUtils.getInstance()
    private val viewModel: MapsHomeViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LocationPermissions.requestLocationPermissions(requireActivity()) {
            Log.d(MapsHomeFragment::class.java.name, "Permission given $it")
        }
        locationUtils = LocationUtils.getInstance(getRootActivity())
        viewModel.fragCreated = (savedInstanceState != null)
        binding.root.setDrag(this)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap?.setOnMarkerClickListener(this)
        googleMap?.setMinZoomPreference(15f)
        googleMap?.let {
            LocationPermissions.isLocationPermissionsGiven(getRootActivity()) { permission ->
                if (permission) {
                    googleMap?.isMyLocationEnabled = true
                    locationUtils?.getFusedLocation { loc ->
                        mapsUtils.moveCameraOnMap(it, latLng = LatLng(loc.latitude, loc.longitude))
                    }
                }
            }
        }

        getCurrentLocation()
        initObservations()
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

    override fun onDrag() {
        if (viewModel.fragCreated)
            viewModel.fragCreated = false
        val currentLatLng = googleMap?.cameraPosition?.target
        val currentBounds = googleMap?.projection?.visibleRegion?.latLngBounds
        viewModel.resetRestaurantsDataState()

        if (currentBounds != null && currentLatLng != null)
            viewModel.getRestaurants(currentLatLng, currentBounds)
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

    private fun getCurrentLocation() {
        if (locationUtils?.isLocationEnabled() == true) {
            locationUtils?.initOnLocationChangeListener { location ->
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

    override fun onMarkerClick(p0: Marker): Boolean {

        return false
    }

    override fun onStop() {
        super.onStop()
        locationUtils?.removeLocationListener()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LocationPermissions.REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    googleMap?.isMyLocationEnabled = true
                    getCurrentLocation()
                }
            }
        }
    }
}
