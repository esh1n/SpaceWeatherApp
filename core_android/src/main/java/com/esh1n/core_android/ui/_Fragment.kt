package com.esh1n.core_android.ui

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager
import com.esh1n.core_android.ui.activity.BaseToolbarActivity

fun Fragment.hideKeyboard() {
    view?.let { hideKeyboard(activity, it.windowToken) }
}

fun Fragment.setTitle(title: String?) {
    (activity as BaseToolbarActivity).setABTitle(title)
}

fun hideKeyboard(context: Context?, windowToken: IBinder) {
    context?.let {
        val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}