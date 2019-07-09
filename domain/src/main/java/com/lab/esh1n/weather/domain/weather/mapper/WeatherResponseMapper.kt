package com.lab.esh1n.weather.domain.weather.mapper

import com.lab.esh1n.data.api.response.WeatherResponse
import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.base.Mapper

class WeatherResponseMapper : Mapper<WeatherResponse, WeatherEntity>() {

    private val dateConverter = EpochDateMapper()

    override fun map(source: WeatherResponse): WeatherEntity {

        return WeatherEntity(
                id = source.id,
                cityName = source.name ?: "",
                temperature = source.main?.temp?:0.0,
                temperatureMin = source.main?.tempMin?:0.0,
                temperatureMax = source.main?.tempMax?:0.0,
                iconId = source.weather?.get(0)?.icon ?: "01d",
                description = source.weather?.get(0)?.description ?: "clear sky",
                windSpeed = source.wind?.speed ?: 0F,
                windDegree = source.wind?.deg?:0,
                pressure = source.main?.pressure?:0,
                humidity = source.main?.humidity ?: 0,
                date = source.dt
        )
    }
}