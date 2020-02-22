package com.esh1n.utils_android.ui

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.esh1n.utils_android.R
import com.google.android.material.snackbar.Snackbar

object SnackbarBuilder {

    fun buildSnack(v: View, message: String, actionText: String, action: View.OnClickListener): Snackbar {
        return buildSnack(
            v,
            message,
            actionText,
            Color.GRAY,
            Color.GRAY,
            Color.BLACK,
            8000,
            action
        )
    }


    fun buildErrorSnack(v: View, message: String, actionText: String, action: View.OnClickListener): Snackbar {
        return buildSnack(
            v,
            message,
            actionText,
            Color.BLACK,
            Color.BLACK,
            Color.RED,
            8000,
            action
        )
    }

    fun buildErrorSnack(v: View, message: String): Snackbar {
        return buildSnack(
            v,
            message,
            "",
            Color.BLACK,
            Color.BLACK,
            Color.RED,
                1000,
            null
        )
    }


    fun showErrorSnackOrToast(activity: Activity, message: String) {
        val view = activity.findViewById<View>(R.id.content)
        if (view == null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        } else {
            buildErrorSnack(view, message).show()
        }
    }

    @JvmOverloads
    fun buildSnack(v: View,
                   message: String,
                   actionText: String = "",
                   textColor: Int = Color.GRAY,
                   actionColor: Int = -1,
                   bgColor: Int = Color.BLACK,
                   duration: Int = 8000,
                   action: View.OnClickListener? = null): Snackbar {


        val snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG)
            .setAction(actionText, action)
            .setActionTextColor(actionColor)
            .setDuration(duration)

        val snackView = snackbar.view
        snackView.setBackgroundColor(bgColor)
        val snackTextView = snackView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView

        snackTextView.apply {
            textSize = 15f
            setTextColor(textColor)
        }

        return snackbar


    }
}