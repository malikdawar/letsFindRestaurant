package com.example.restaurants.base

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.restaurants.core.utils.DialogUtils
import com.example.restaurants.ui.MainActivity
import com.kaopiz.kprogresshud.KProgressHUD

/**
 * The BaseFragment.kt
 * @author Malik Dawar, malikdawar@hotmail.com
 */
abstract class BaseFragment : Fragment {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected lateinit var progressDialog: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = DialogUtils.showProgressDialog(requireContext(), cancelable = false)
    }

    fun getRootActivity(): MainActivity = activity as MainActivity
}