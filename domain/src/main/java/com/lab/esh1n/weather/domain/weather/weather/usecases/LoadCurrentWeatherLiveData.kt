package com.lab.esh1n.weather.domain.weather.weather.usecases

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.weather.repository.WeatherRepository
import io.reactivex.Observable


class LoadCurrentWeatherLiveDataUseCase(private val weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler) :
        UseCase<Observable<Resource<WeatherEntity>>, Unit>(errorsHandler) {

    override fun perform(args: Unit): Observable<Resource<WeatherEntity>> {
        return weatherRepository.getCurrentWeather()
                .map { users -> Resource.success(users) }
                .onErrorReturn { error -> Resource.error(errorsHandler.handle(error)) }
    }
}
