package com.lab.esh1n.weather.domain.weather.usecases

import com.lab.esh1n.weather.data.cache.entity.SunsetSunriseTimezonePlaceEntry
import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import io.reactivex.Observable


class LoadCurrentWeatherUseCase(private val weatherRepository: WeatherRepository) {

    fun perform(): Observable<Pair<SunsetSunriseTimezonePlaceEntry, List<WeatherWithPlace>>> {
        return Observable.combineLatest(
            weatherRepository.getCurrentWeatherWithForecast(),
            weatherRepository.getCurrentPlaceSunsetAndSunrise()
        ) { weathers, sunset -> Pair(sunset, weathers) }
    }
}
