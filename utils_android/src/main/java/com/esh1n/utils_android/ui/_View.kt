package com.esh1n.utils_android.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun View.setVisibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}


fun ViewGroup.inflate(res: Int): View {
    return LayoutInflater.from(this.context).inflate(res, this, false)
}




