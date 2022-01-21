package com.example.restaurants.core.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.restaurants.App
import com.example.restaurants.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*


class LocationPermissions(val context: Context) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mLocationCallback: LocationCallback? = null
    private var mLocationRequest: LocationRequest? = null

    companion object {
        const val GOOGLE_PLAY_SERVICES_REQUEST = 1001
    }

    private fun setupApiClient() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)
    }

    fun getUserLocation(onLocationAvailable: (Location) -> Unit) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null)
                        onLocationAvailable(location)
                    else
                        createLocationRequest(onLocationAvailable)
                }
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun createLocationRequest(onLocationAvailable: (Location) -> Unit) {
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.interval = 5000.toLong()
        mLocationRequest?.fastestInterval = 5000.toLong()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    onLocationAvailable(location)
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallback as LocationCallback,
            Looper.myLooper()!!
        )
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(context.applicationContext)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, GOOGLE_PLAY_SERVICES_REQUEST)
            } else {
                activity?.finish()
            }
            return false
        }
        return true
    }

    private fun stopLocationUpdates() {
        if (mLocationCallback != null)
            fusedLocationClient.removeLocationUpdates(mLocationCallback!!)
    }
}