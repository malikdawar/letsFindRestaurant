package com.example.restaurants.core.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.restaurants.App
import java.util.*

/**
 * Extension function to show toast message
 * @author Dawar Malik.
 */
fun Any.showToastMsg(message: String) {
    Toast.makeText(App.getAppContext(), message, Toast.LENGTH_SHORT).show()
}

/**
 * An Extension to make view Visible
 * @author Dawar Malik.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * An Extension to make view Gone
 * @author Dawar Malik.
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Returns [Boolean] based on current time.
 * Returns true if hours are between 06:00 pm - 07:00 am
 */
fun checkIsNight(): Boolean {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return (currentHour <= 7 || currentHour >= 18)
}