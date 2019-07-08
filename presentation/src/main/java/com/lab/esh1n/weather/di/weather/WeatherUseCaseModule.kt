package com.lab.esh1n.weather.di.weather

import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import com.lab.esh1n.weather.domain.weather.usecases.FetchAndSaveCurrentPlaceWeatherUseCase
import com.lab.esh1n.weather.domain.weather.usecases.FetchAndSaveWeatherUseCase
import com.lab.esh1n.weather.domain.weather.usecases.LoadCurrentWeatherUseCase
import dagger.Module
import dagger.Provides

@Module
class WeatherUseCaseModule {

    @Provides
    fun provideFetchAndSaveWeatherUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): FetchAndSaveWeatherUseCase {
        return FetchAndSaveWeatherUseCase(weatherRepository, errorsHandler)
    }

    @Provides
    fun provideFetchAndSaveCurrentPlaceWeatherUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): FetchAndSaveCurrentPlaceWeatherUseCase {
        return FetchAndSaveCurrentPlaceWeatherUseCase(weatherRepository, errorsHandler)
    }

    @Provides
    fun provideLoadWeatherByCityFromDBUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): LoadCurrentWeatherUseCase {
        return LoadCurrentWeatherUseCase(weatherRepository, errorsHandler)
    }
}