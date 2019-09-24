package com.lab.esh1n.data.api.response

import com.google.gson.annotations.SerializedName


data class SnowResponse(
        @field:SerializedName("3h")
        val snow3h: Float? = null,
        @field:SerializedName("1h")
        val snow1h: Float? = null
)