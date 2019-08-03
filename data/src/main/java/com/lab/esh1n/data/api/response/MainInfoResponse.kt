package com.lab.esh1n.data.api.response

import com.google.gson.annotations.SerializedName

data class MainInfoResponse(

        @field:SerializedName("temp")
	val temp: Double? = null,

        @field:SerializedName("temp_min")
	val tempMin: Double? = null,

        @field:SerializedName("humidity")
        val humidity: Float? = null,

        @field:SerializedName("pressure")
        val pressure: Float? = null,

        @field:SerializedName("temp_max")
	val tempMax: Double? = null
)