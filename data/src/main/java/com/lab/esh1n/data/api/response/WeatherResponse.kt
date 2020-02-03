package com.lab.esh1n.data.api.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

        @field:SerializedName("dt")
        val dt: Long,

        @field:SerializedName("visibility")
        val visibility: Int? = null,

        @field:SerializedName("weather")
        val weather: List<WeatherItemResponse?>? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("timezone")
        val timezone: Int? = null,

        @field:SerializedName("cod")
        val cod: Int? = null,

        @field:SerializedName("main")
        val main: MainInfoResponse? = null,

        @field:SerializedName("clouds")
        val clouds: CloudsResponse? = null,

        @field:SerializedName("rain")
        val rain: RainResponse? = null,

        @field:SerializedName("snow")
        val snow: SnowResponse? = null,

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("wind")
        val wind: WindResponse? = null,

        @field:SerializedName("sys")
        val sys: SysItem? = null
)