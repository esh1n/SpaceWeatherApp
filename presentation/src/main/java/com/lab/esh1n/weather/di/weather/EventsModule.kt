package com.lab.esh1n.weather.di.weather

import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.weather.FetchAndSaveWeatherUseCase
import com.lab.esh1n.weather.domain.weather.LoadWeatherByCityFromDBUseCase
import dagger.Module
import dagger.Provides

@Module
class EventsModule {

    @Provides
    fun provideFetchAndSaveWeatherUseCase(db: WeatherDB, api: APIService, errorsHandler: ErrorsHandler): FetchAndSaveWeatherUseCase {
        return FetchAndSaveWeatherUseCase(api, db.weatherDAO(), errorsHandler)
    }

    @Provides
    fun provideLoadWeatherByCityFromDBUseCase(db: WeatherDB, errorsHandler: ErrorsHandler): LoadWeatherByCityFromDBUseCase {
        return LoadWeatherByCityFromDBUseCase(db.weatherDAO(), errorsHandler)
    }
}