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
import com.example.restaurants.data.DataState
import com.example.restaurants.data.model.Restaurant
import com.example.restaurants.databinding.FragmentHomeBinding

@RuntimePermissions
@AndroidEntryPoint
class MapsHomeFragment : BaseFragment(R.layout.fragment_home), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    IDragCallback {

    private var googleMap: GoogleMap? = null
    private val viewModel: MapsHomeViewModel by viewModels()
    private val openSettingsActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            getCurrentLocation()
        }

    private val openLocationSettingsScreen =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            getCurrentLocation()
        }

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

        viewModel.restaurantsDataState.observe(viewLifecycleOwner) { photos ->

        }
    }

    private fun observerRestaurants() {

        viewModel.restaurantsDataState.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is DataState.Success -> {
                        getRootActivity().handleLoading(false)
                        renderMarkers(it.data)
                    }
                    is DataState.Error -> {
                        getRootActivity().handleLoading(false)
                        if (it.error is Failure.NetworkConnection)
                            getRootActivity().displayError(getString(R.string.no_internet_connection))
                        else
                            getRootActivity().displayError(getString(R.string.general_error))
                    }
                    is DataState.Loading -> getRootActivity().handleLoading(true)
                }
            }
        )
    }

    private fun renderMarkers(venues: List<Restaurant>) {
        val markersToBeDisplayed = ArrayList<Restaurant>()
        val mainList = viewModel.markers.values
        venues.forEach {
            if (!mainList.contains(it))
                markersToBeDisplayed.add(it)
        }
        if (viewModel.fragCreated && markersToBeDisplayed.isEmpty()) {
            mainList.forEach {
                val markerLoc = LatLng(it.latitude, it.longitude)
                val marker = googleMap?.addMarker(MarkerOptions().position(markerLoc).title(it.name))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(markerLoc))
            }
        } else {
            markersToBeDisplayed.forEach {
                val markerLoc = LatLng(it.latitude, it.longitude)
                val marker = googleMap?.addMarker(MarkerOptions().position(markerLoc).title(it.name))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(markerLoc))
                if (marker != null)
                    viewModel.markers[marker] = it
            }
        }
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getCurrentLocation() {
        if (isLocationEnabled()) {
            getUserLocation { location ->
                Timber.e("available lat is %s , lon is %s", location.latitude, location.longitude)
                val currentBounds = googleMap?.projection?.visibleRegion?.latLngBounds
                val latlng = LatLng(location.latitude, location.longitude)
                currentBounds?.let { viewModel.getRestaurants(Dto(latlng, it)) }

            }
        } else {
            MaterialAlertDialogBuilder(getRootActivity())
                .setTitle(getString(R.string.enable_location))
                .setMessage(R.string.location_disabled)
                .setPositiveButton(getString(R.string.enable)) { dialog, _ ->
                    openSettingsScreen()

                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.ignore)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        MaterialAlertDialogBuilder(getRootActivity())
            .setMessage(R.string.permission_alert)
            .setPositiveButton(getString(R.string.accept)) { dialog, _ ->
                request.proceed()
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.deny)) { dialog, _ ->
                request.cancel()
                dialog.dismiss()
            }.show()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationDenied() {
        Toast.makeText(activity, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT)
            .show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationNeverAskAgain() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        openSettingsActivity.launch(intent)
    }

    private fun openSettingsScreen() {
        openLocationSettingsScreen.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap?.setOnMarkerClickListener(this)
        googleMap?.setMinZoomPreference(15f)
        googleMap?.isMyLocationEnabled = true
        observerRestaurants()
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
