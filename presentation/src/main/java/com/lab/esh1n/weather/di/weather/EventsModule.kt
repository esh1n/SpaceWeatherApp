package com.lab.esh1n.weather.di.weather

import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.events.FetchAndSaveEventsUseCase
import com.lab.esh1n.weather.domain.events.GetEventUseCase
import dagger.Module
import dagger.Provides

@Module
class EventsModule {

    @Provides
    fun provideEventsInDBUseCase(db: WeatherDB, errorsHandler: ErrorsHandler): GetEventsInDBUseCase {
        return GetEventsInDBUseCase(db.repositoriesDAO(), errorsHandler)
    }

    @Provides
    fun provideFetchAndSaveEventsUseCase(db: WeatherDB, api: APIService, errorsHandler: ErrorsHandler): FetchAndSaveEventsUseCase {
        return FetchAndSaveEventsUseCase(api, db.repositoriesDAO(), errorsHandler)
    }

    @Provides
    fun provideEventDetailsUseCase(db: WeatherDB, errorsHandler: ErrorsHandler): GetEventUseCase {
        return GetEventUseCase(db.repositoriesDAO(), errorsHandler)
    }
}