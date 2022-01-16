package com.example.restaurants.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurants.core.utils.InternetMonitor

/**
 * The BaseActivity.kt
 * @author Malik Dawar, malikdawar@hotmail.com
 */

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var internetMessageBinding: UiInternetMonitorBinding

    //internet monitor, to monitor the internet state to to update the app
    private val internetMonitor = InternetMonitor(object :
        InternetMonitor.OnInternetConnectivityListener {
        override fun onInternetAvailable() {
            //getting the data in Background thread and showing it to the view on UI thread
            runOnUiThread {
                internetMessageBinding.topConnectionShower.gone()
            }
        }

        override fun onInternetLost() {
            //onInternetLost, getting the data in Background thread and showing it to the view on UI thread
            runOnUiThread {
                internetMessageBinding.topConnectionShower.visible()
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        internetMessageBinding = UiInternetMonitorBinding.inflate(layoutInflater)
    }

    //registering the internet monitor on resume
    override fun onResume() {
        super.onResume()
        internetMonitor.register(this)
    }

    //unregistering the internet monitor on pause
    override fun onPause() {
        super.onPause()
        internetMonitor.unregister(this)
    }
}