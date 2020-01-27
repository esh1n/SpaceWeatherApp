package com.lab.esh1n.weather.domain.weather.weather.usecases

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.data.cache.entity.SunsetSunrisePlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.weather.WeatherRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction


class LoadCurrentWeatherUseCase(private val weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler) :
        UseCase<Observable<Resource<Pair<SunsetSunrisePlaceEntry, List<WeatherWithPlace>>>>, Unit>(errorsHandler) {

    override fun perform(args: Unit): Observable<Resource<Pair<SunsetSunrisePlaceEntry, List<WeatherWithPlace>>>> {
        return Observable.combineLatest(weatherRepository
                .getCurrentWeatherWithForecast(), weatherRepository.getCurrentPlaceSunsetAndSunrise(),
                BiFunction<List<WeatherWithPlace>, SunsetSunrisePlaceEntry, Pair<SunsetSunrisePlaceEntry, List<WeatherWithPlace>>> { weathers, sunset ->
                    return@BiFunction Pair(sunset, weathers)
                })
                .map { users -> Resource.success(users) }
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }
}
