package com.lab.esh1n.weather.data.api.response

import com.google.gson.annotations.SerializedName

data class MainInfoResponse(

        @field:SerializedName("temp")
        val temp: Double? = null,

        @field:SerializedName("temp_min")
        val tempMin: Double? = null,

        @field:SerializedName("temp_max")
        val tempMax: Double? = null,

        @field:SerializedName("humidity")
        val humidity: Float? = null,

        @field:SerializedName("pressure")
        val pressure: Float? = null,

        @field:SerializedName("sea_level")
        val seaLevel: Double? = null,

        @field:SerializedName("grnd_level")
        val grndLevel: Double? = null,

        @field:SerializedName("temp_kf")
        val tempKf: Double? = null

)