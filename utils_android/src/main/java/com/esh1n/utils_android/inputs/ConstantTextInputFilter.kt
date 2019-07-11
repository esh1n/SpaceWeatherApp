package com.esh1n.utils_android.inputs

import android.text.*

class ConstantTextInputFilter(private val prefix: String) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val newStart = Math.max(prefix.length, dstart)
        val newEnd = Math.max(prefix.length, dend)
        if (newStart != dstart || newEnd != dend) {
            val builder = SpannableStringBuilder(dest)
            builder.replace(newStart, newEnd, source)
            if (source is Spanned) {
                TextUtils.copySpansFrom(source, 0, source.length, null, builder, newStart)
            }
            Selection.setSelection(builder, newStart + source.length)
            return builder
        } else {
            return null
        }
    }
}
