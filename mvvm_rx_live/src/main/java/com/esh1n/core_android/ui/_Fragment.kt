package com.esh1n.core_android.ui

import android.app.Activity
import android.content.Context
import android.os.IBinder

import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.esh1n.core_android.ui.activity.BaseToolbarActivity

fun Fragment.hideKeyboard() {
    view?.let { hideKeyboard(activity, it.windowToken) }
}

fun Fragment.setTitle(title: CharSequence) {
    (activity as BaseToolbarActivity).setABTitle(title)
}

fun hideKeyboard(context: Context?, windowToken: IBinder) {
    context?.let {
        val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}