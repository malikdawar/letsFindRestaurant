package com.example.restaurants.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.restaurants.core.utils.DialogUtils
import com.example.restaurants.ui.MainActivity
import com.kaopiz.kprogresshud.KProgressHUD

/**
 * The BaseFragment.kt
 * @author Malik Dawar, malikdawar@hotmail.com
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var mainActivity: MainActivity
    protected lateinit var progressDialog: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        progressDialog = DialogUtils.showProgressDialog(mainActivity, cancelable = false)
    }
}