package com.lab.esh1n.weather.data.api.response

import com.google.gson.annotations.SerializedName

data class WindResponse(

        @field:SerializedName("deg")
        val deg: Double? = null,

        @field:SerializedName("speed")
        val speed: Double? = null
)