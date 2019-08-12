package com.lab.esh1n.weather.domain.weather.weather.usecases

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.weather.WeatherRepository
import io.reactivex.Observable


class LoadCurrentWeatherUseCase(private val weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler) :
        UseCase<Observable<Resource<List<WeatherWithPlace>>>, Unit>(errorsHandler) {

    override fun perform(args: Unit): Observable<Resource<List<WeatherWithPlace>>> {
        return weatherRepository.getCurrentWeatherWithForecast()
                .map { users -> Resource.success(users) }
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }
}
