package com.lab.esh1n.data.cache.entity

import java.util.*

class UpdatePlaceEntry(val id: Int, val sunset: Date, val sunrise: Date) {
    companion object {
        fun createEmpty() = UpdatePlaceEntry(-1, Date(), Date())
    }
}