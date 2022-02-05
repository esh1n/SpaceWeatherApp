package com.lab.esh1n.weather.domain.weather.mapper

import com.esh1n.core_android.map.ListMapper
import com.lab.esh1n.weather.data.api.response.ForecastItemResponse
import com.lab.esh1n.weather.data.cache.entity.Temperature
import com.lab.esh1n.weather.data.cache.entity.WeatherEntry
import com.lab.esh1n.weather.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.data.converter.WindDegreeConverter
import com.lab.esh1n.weather.domain.TemperatureUnit
import com.lab.esh1n.weather.domain.Units
import java.util.*

class ForecastWeatherListMapper(val placeId: Int, private val unitMetric: Units) : ListMapper<ForecastItemResponse, WeatherEntry>() {

    private val dateConverter = EpochDateListMapper()
    private val temperatureUnit = TemperatureUnit.getBySystem(unitMetric)
    override fun map(source: ForecastItemResponse): WeatherEntry {
        return WeatherEntry(
                placeId = placeId,
                temperature = Temperature(source.main?.temp ?: 0.0, temperatureUnit),
                temperatureMin = Temperature(source.main?.tempMin ?: 0.0, temperatureUnit),
                temperatureMax = Temperature(source.main?.tempMax ?: 0.0, temperatureUnit),
                iconId = source.weather?.get(0)?.icon ?: "01d",
                description = source.weather?.get(0)?.description ?: "clear sky",
                windSpeed = WindSpeed(source.wind?.speed ?: 0.0, unitMetric),
                windDegree = WindDegreeConverter.mapToWindDegree(source.wind?.deg ?: 0.0),
                pressure = source.main?.pressure ?: 0F,
                humidity = source.main?.humidity ?: 0F,
                date = dateConverter.map(source.dt ?: Date().time),
                dateTxt = source.dtTxt ?: "",
                snow = source.snow?.snow3h ?: 0f,
                rain = source.rain?.rain3h ?: 0f,
                cloudiness = source.clouds?.all ?: 0,
                dateSeconds = source.dt ?: 0
        )
    }
}