package com.lab.esh1n.weather.di.weather

import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import com.lab.esh1n.weather.domain.weather.usecases.FetchAndSaveWeatherUseCase
import com.lab.esh1n.weather.domain.weather.usecases.LoadWeatherByCityFromDBUseCase
import dagger.Module
import dagger.Provides

@Module
class WeatherUseCaseModule {

    @Provides
    fun provideFetchAndSaveWeatherUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): FetchAndSaveWeatherUseCase {
        return FetchAndSaveWeatherUseCase(weatherRepository, errorsHandler)
    }

    @Provides
    fun provideLoadWeatherByCityFromDBUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): LoadWeatherByCityFromDBUseCase {
        return LoadWeatherByCityFromDBUseCase(weatherRepository, errorsHandler)
    }
}