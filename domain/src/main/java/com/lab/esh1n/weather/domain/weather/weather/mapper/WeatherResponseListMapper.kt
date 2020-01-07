package com.lab.esh1n.weather.domain.weather.weather.mapper

import com.esh1n.core_android.map.ListMapper
import com.lab.esh1n.data.api.response.WeatherResponse
import com.lab.esh1n.data.cache.entity.WeatherEntry

class WeatherResponseListMapper(private val placeId: Int) : ListMapper<WeatherResponse, WeatherEntry>() {

    private val dateConverter = EpochDateListMapper()

    override fun map(source: WeatherResponse): WeatherEntry {
        return WeatherEntry(
                placeId = placeId,
                temperature = source.main?.temp ?: 0.0,
                temperatureMin = source.main?.tempMin ?: 0.0,
                temperatureMax = source.main?.tempMax ?: 0.0,
                iconId = source.weather?.get(0)?.icon ?: "01d",
                description = source.weather?.get(0)?.description ?: "clear sky",
                windSpeed = source.wind?.speed ?: 0.0,
                windDegree = source.wind?.deg ?: 0.0,
                pressure = source.main?.pressure ?: 0F,
                humidity = source.main?.humidity ?: 0F,
                date = dateConverter.map(source.dt),
                dateTxt = "Current",
                snow = source.snow?.snow3h ?: source.snow?.snow1h ?: 0f,
                rain = source.rain?.rain3h ?: source.rain?.rain1h ?: 0f,
                cloudiness = source.clouds?.all ?: 0,
                dateSeconds = source.dt
        )
    }

}