package com.example.restaurants.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.example.restaurants.R
import com.example.restaurants.base.BaseActivity
import com.example.restaurants.core.extensions.replaceFragmentSafely
import com.example.restaurants.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * The MainActivity.kt, Main activity class, launcher activity
 * @author Malik Dawar, malikdawar@hotmail.com
 */

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragmentSafely(HomeFragment())
    }

}