package com.lab.esh1n.weather.data.api.response.places

import com.google.gson.annotations.SerializedName
import com.lab.esh1n.weather.data.api.response.CoordResponse

data class AssetCityResponse(

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("coord")
        val coord: CoordResponse? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: Int? = null
)