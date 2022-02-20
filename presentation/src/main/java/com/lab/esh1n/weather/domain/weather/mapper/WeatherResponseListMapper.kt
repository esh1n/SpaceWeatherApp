package com.lab.esh1n.weather.domain.weather.mapper

import com.esh1n.core_android.map.ListMapper
import com.lab.esh1n.weather.data.api.response.WeatherResponse
import com.lab.esh1n.weather.data.cache.entity.Temperature
import com.lab.esh1n.weather.data.cache.entity.WeatherEntry
import com.lab.esh1n.weather.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.data.converter.WindDegreeConverter
import com.lab.esh1n.weather.domain.prefs.TemperatureUnit
import com.lab.esh1n.weather.domain.prefs.Units

class WeatherResponseListMapper(private val placeId: Int, private val units: Units) : ListMapper<WeatherResponse, WeatherEntry>() {

    private val temperatureUnit = TemperatureUnit.getBySystem(units)
    private val dateConverter = EpochDateListMapper()

    override fun map(source: WeatherResponse): WeatherEntry {
        return WeatherEntry(
                placeId = placeId,
                temperature = Temperature(source.main?.temp ?: 0.0, temperatureUnit),
                temperatureMin = Temperature(source.main?.tempMin ?: 0.0, temperatureUnit),
                temperatureMax = Temperature(source.main?.tempMax ?: 0.0, temperatureUnit),
                iconId = source.weather?.get(0)?.icon ?: "01d",
                description = source.weather?.get(0)?.description ?: "clear sky",
                windSpeed = WindSpeed(source.wind?.speed ?: 0.0, units),
                windDegree = WindDegreeConverter.mapToWindDegree(source.wind?.deg ?: 0.0),
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