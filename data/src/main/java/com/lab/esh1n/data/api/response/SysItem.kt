package com.lab.esh1n.data.api.response

import com.google.gson.annotations.SerializedName

data class SysItem(
        @field:SerializedName("sunset")
        val sunrise: Long? = null,
        @field:SerializedName("sunset")
        val sunset: Long? = null,
        @field:SerializedName("country")
        val country: String? = null
)