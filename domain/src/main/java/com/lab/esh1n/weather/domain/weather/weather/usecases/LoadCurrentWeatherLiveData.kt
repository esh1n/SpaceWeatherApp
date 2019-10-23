package com.lab.esh1n.weather.domain.weather.weather.usecases

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.data.cache.entity.UpdatePlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.weather.WeatherRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction


class LoadCurrentWeatherUseCase(private val weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler) :
        UseCase<Observable<Resource<Pair<UpdatePlaceEntry, List<WeatherWithPlace>>>>, Unit>(errorsHandler) {

    override fun perform(args: Unit): Observable<Resource<Pair<UpdatePlaceEntry, List<WeatherWithPlace>>>> {
        return weatherRepository
                .getCurrentWeatherWithForecast()
                .zipWith(weatherRepository.getCurrentPlaceSunsetAndSunrise()
                        , BiFunction<List<WeatherWithPlace>, UpdatePlaceEntry, Pair<UpdatePlaceEntry, List<WeatherWithPlace>>> { weathers, sunset ->
                    return@BiFunction Pair(sunset, weathers)
                })
                .map { users -> Resource.success(users) }
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }
}
