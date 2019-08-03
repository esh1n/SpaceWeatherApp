package com.lab.esh1n.data.api.response

import com.google.gson.annotations.SerializedName

data class WindResponse(

		@field:SerializedName("deg")
		val deg: Float? = null,

		@field:SerializedName("speed")
	val speed: Float? = null
)