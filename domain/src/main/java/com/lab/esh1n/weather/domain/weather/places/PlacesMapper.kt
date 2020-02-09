package com.lab.esh1n.weather.domain.weather.places

import com.esh1n.core_android.map.ListMapper
import com.lab.esh1n.data.api.response.places.AssetCityResponse
import com.lab.esh1n.data.cache.entity.PlaceEntry

class PlacesMapper : ListMapper<AssetCityResponse, PlaceEntry>() {
    override fun map(source: AssetCityResponse): PlaceEntry {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}