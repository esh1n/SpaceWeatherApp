package com.lab.esh1n.weather.data.api.response

import com.google.gson.annotations.SerializedName

data class CloudsResponse(

	@field:SerializedName("all")
	val all: Int? = null
)