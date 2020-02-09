package com.lab.esh1n.data.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlaceAsset(@SerializedName("id")
                 @Expose
                 val id: Int,
                 @SerializedName("name")
                 @Expose
                 var name: String? = null,
                 @SerializedName("country")
                 @Expose
                 var country: String? = null,
                 @SerializedName("coord")
                 @Expose
                 var coord: CoordResponse? = null)