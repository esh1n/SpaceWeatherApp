package com.esh1n.utils_android.ui

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.esh1n.utils_android.R

object DialogUtil {

    fun showYesNoDialog(
        context: Context, title: String?, message: String?,
        yesButton: String, noButton: String, yesAction: (() -> Unit)?
    ) {
        buildDialog(context, title, message, yesButton, noButton, yesAction, null)
            .show()
    }

    fun showYesNoDialog(context: Context, message: String, yesButton: String, yesAction: (() -> Unit)?) {
        buildDialog(
            context,
            null,
            message,
            yesButton,
            context.getString(R.string.text_cancel),
            yesAction,
            null
        ).show()
    }

    fun showYesNoTitleDialog(context: Context, title: String, message: String, yesAction: (() -> Unit)?) {
        buildDialog(
            context,
            title,
            message,
            context.getString(R.string.text_ok),
            context.getString(R.string.text_cancel),
            yesAction,
            null
        ).show()
    }

    fun showYesNoDialog(
        context: Context,
        title: String?,
        message: String?,
        yesButton: String,
        noButton: String,
        yesAction: (() -> Unit)?,
        noAction: (() -> Unit)?
    ) {
        buildDialog(
            context,
            title,
            message,
            yesButton,
            noButton,
            yesAction,
            noAction
        ).show()
    }

    fun showInfoDialog(
        context: Context, title: String?, message: String?,
        infoButton: String
    ) {
        buildDialog(context, title, message, null, infoButton, null, null).show()
    }

    fun showAlertDialog(context: Context, title: String?, message: String?) {
        buildDialog(context, title, message, null, null, null, null).show()
    }

    fun buildListDialog(context: Context, title: String, itemsResId: Int, selectOption: (Int) -> Unit): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setItems(itemsResId) { _, which ->
            selectOption.invoke(which)
        }
        return builder.create()
    }

    private fun buildDialog(
        context: Context, title: String?, message: String?,
        yesButton: String?, noButton: String?,
        yesAction: (() -> Unit)?,
        noAction: (() -> Unit)?
    ): AlertDialog {

        val builder = AlertDialog.Builder(context)
        if (!title.isNullOrEmpty()) {
            builder.setTitle(title)
        }

        if (!message.isNullOrBlank()) {
            builder.setMessage(message)
        }

        if (!yesButton.isNullOrBlank()) {
            builder.setPositiveButton(yesButton) { dialog, _ ->
                yesAction?.invoke()
                dialog.dismiss()
            }
        }

        if (!noButton.isNullOrBlank()) {
            builder.setNegativeButton(noButton) { dialog, _ ->
                dialog.dismiss()
                noAction?.invoke()
            }
        }
        builder.setCancelable(false)

        val alertDialog = builder.create()
        val focusableView = alertDialog.findViewById<View>(android.R.id.title)
        (focusableView ?: alertDialog.findViewById<View>(android.R.id.content))?.also {
            it.isFocusable = true
            it.isFocusableInTouchMode = true
            it.requestFocus()
        }

        return alertDialog
    }
}