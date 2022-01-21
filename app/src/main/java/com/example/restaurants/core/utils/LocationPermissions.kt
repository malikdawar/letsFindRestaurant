package com.example.restaurants.core.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.restaurants.App

object LocationPermissions {

    private const val REQUEST_CODE_LOCATION = 123

    fun requestLocationPermissions(
        activity: Activity,
        locationPermission: (Boolean) -> Unit
    ) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (ContextCompat.checkSelfPermission(
                App.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                App.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermission.invoke(true)
        } else {
            ActivityCompat.requestPermissions(
                activity, permissions,
                REQUEST_CODE_LOCATION
            )
        }
    }
}