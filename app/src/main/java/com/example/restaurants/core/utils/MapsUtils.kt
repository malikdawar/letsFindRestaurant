package com.example.restaurants.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.restaurants.App
import com.example.restaurants.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class MapsUtils private constructor(private val context: Context) {

    fun drawMarker(
        googleMap: GoogleMap?,
        location: LatLng?,
        resDrawable: Int = R.drawable.pin,
        title: String? = null
    ): Marker? {
        googleMap ?: return null

        val circleDrawable = ContextCompat.getDrawable(context, resDrawable)
        val markerIcon = getMarkerIconFromDrawable(circleDrawable)
        return googleMap.addMarker(
            MarkerOptions()
                .position(location!!)
                .title(title)
                .icon(markerIcon)
        )
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable?): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun moveCameraOnMap(
        googleMap: GoogleMap?,
        zoom: Float = 15.5f,
        animate: Boolean = true,
        latLng: LatLng
    ) {
        if (animate) {
            googleMap?.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude)).zoom(
                        zoom
                    ).build()
                )
            )
        } else {
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
            googleMap?.animateCamera(
                CameraUpdateFactory.zoomTo(zoom)
            )
        }
    }

    companion object {
        fun getInstance(): MapsUtils {
            return MapsUtils(App.getAppContext())
        }
    }
}
