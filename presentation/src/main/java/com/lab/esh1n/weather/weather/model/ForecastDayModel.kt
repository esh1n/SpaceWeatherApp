package com.lab.esh1n.weather.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ForecastDayModel(val dayDescription: String, val dayDate: Date, val placeId: Int, val timezone: String) : Parcelable