package com.lab.esh1n.data.api.response

import com.google.gson.annotations.SerializedName

data class ForecastItemResponse(
        val dt: Long? = null,
        val rain: RainResponse? = null,
        @field:SerializedName("dt_txt")
        val dtTxt: String? = null,
        val weather: List<WeatherItemResponse?>? = null,
        val main: MainInfoResponse? = null,
        val clouds: CloudsResponse? = null,
        val wind: WindResponse? = null,
        val snow: SnowResponse? = null
)
