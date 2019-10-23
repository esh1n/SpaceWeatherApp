package com.lab.esh1n.data.api.response

import com.google.gson.annotations.SerializedName

data class SysItem(
        @field:SerializedName("sunrise")
        val sunrise: Long? = null,
        @field:SerializedName("sunset")
        val sunset: Long? = null
)