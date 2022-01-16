package com.example.restaurants.core.extensions

import com.example.restaurants.App
import com.example.restaurants.R

/**
 * The StringExtension.kt
 * @author Malik Dawar, malikdawar@hotmail.com
 */


/**
 * Extension function to noNetworkErrorMessage
 * @author Dawar Malik.
 */
fun noNetworkErrorMessage() =
    App.getAppContext().getString(R.string.message_no_network_connected_str)

/**
 * Extension function to somethingWentWrong
 * @author Dawar Malik.
 */
fun somethingWentWrong() = App.getAppContext().getString(R.string.message_something_went_wrong_str)