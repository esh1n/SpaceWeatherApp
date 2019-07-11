package com.esh1n.utils_android

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


fun copyTextToClipboard(context: Context, text: String, label: String = "") {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText(label, text)
    clipboard?.primaryClip = clip
}