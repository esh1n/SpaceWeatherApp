package com.esh1n.utils_android.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

fun View.setVisibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun Context.getImage(iconId: String, prefix: String = ""): Int {
    val res = "${prefix}status_$iconId"
    return resources.getIdentifier(res, "drawable", packageName)
}

fun ViewGroup.inflate(res: Int): View {
    return LayoutInflater.from(this.context).inflate(res, this, false)
}

fun Context.getColorCompat(colorResource: Int): Int {
    return ContextCompat.getColor(this, colorResource)
}
