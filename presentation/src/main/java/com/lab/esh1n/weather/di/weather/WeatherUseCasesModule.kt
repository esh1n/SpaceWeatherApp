package com.lab.esh1n.weather.di.weather

import com.esh1n.core_android.error.ErrorsHandler
import com.lab.esh1n.weather.domain.weather.places.PlacesRepository
import com.lab.esh1n.weather.domain.weather.places.usecase.GetAllPlacesUse
import com.lab.esh1n.weather.domain.weather.places.usecase.UpdateCurrentPlaceUseCase
import com.lab.esh1n.weather.domain.weather.weather.WeatherRepository
import com.lab.esh1n.weather.domain.weather.weather.usecases.FetchAndSaveCurrentPlaceWeatherUseCase
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadCurrentWeatherUseCase
import dagger.Module
import dagger.Provides

@Module
class WeatherUseCasesModule {


    @Provides
    fun provideFetchAndSaveCurrentPlaceWeatherUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): FetchAndSaveCurrentPlaceWeatherUseCase {
        return FetchAndSaveCurrentPlaceWeatherUseCase(weatherRepository, errorsHandler)
    }

    @Provides
    fun provideLoadCurrentWeatherLiveDataUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): LoadCurrentWeatherUseCase {
        return LoadCurrentWeatherUseCase(weatherRepository, errorsHandler)
    }

    @Provides
    fun provideLoadAllPlacesUseCase(placesRepository: PlacesRepository, errorsHandler: ErrorsHandler): GetAllPlacesUse {
        return GetAllPlacesUse(placesRepository, errorsHandler)
    }

    @Provides
    fun provideUpdateCurrentPlaceUseCase(placesRepository: PlacesRepository, weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): UpdateCurrentPlaceUseCase {
        return UpdateCurrentPlaceUseCase(placesRepository, weatherRepository, errorsHandler)
    }

}