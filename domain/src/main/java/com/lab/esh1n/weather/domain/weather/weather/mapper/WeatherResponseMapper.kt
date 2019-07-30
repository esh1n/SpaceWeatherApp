package com.lab.esh1n.weather.domain.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.lab.esh1n.data.api.response.WeatherResponse
import com.lab.esh1n.data.cache.entity.WeatherEntry

class WeatherResponseMapper(val placeId: Int) : Mapper<WeatherResponse, WeatherEntry>() {

    private val dateConverter = EpochDateMapper()

    override fun map(source: WeatherResponse): WeatherEntry {

        return WeatherEntry(
                placeId = placeId,
                temperature = source.main?.temp?:0.0,
                temperatureMin = source.main?.tempMin?:0.0,
                temperatureMax = source.main?.tempMax?:0.0,
                iconId = source.weather?.get(0)?.icon ?: "01d",
                description = source.weather?.get(0)?.description ?: "clear sky",
                windSpeed = source.wind?.speed ?: 0F,
                windDegree = source.wind?.deg?:0,
                pressure = source.main?.pressure?:0,
                humidity = source.main?.humidity ?: 0,
                date = dateConverter.map(source.dt)
        )
    }
}