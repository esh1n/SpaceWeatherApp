package com.lab.esh1n.data.api.response

import com.google.gson.annotations.SerializedName

data class RainResponse(
        @field:SerializedName("3h")
        val rain3h: Int? = null,

        @field:SerializedName("1h")
        val rain1h: Int? = null
)
