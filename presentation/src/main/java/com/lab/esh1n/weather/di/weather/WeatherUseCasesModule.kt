package com.lab.esh1n.weather.di.weather

import com.esh1n.core_android.error.ErrorsHandler
import com.lab.esh1n.weather.domain.places.PlacesRepository
import com.lab.esh1n.weather.domain.places.usecase.*
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import com.lab.esh1n.weather.domain.weather.usecases.LoadCurrentWeatherSingleUseCase
import com.lab.esh1n.weather.domain.weather.usecases.LoadDayWeatherUseCase
import com.lab.esh1n.weather.domain.weather.usecases.LoadPlaceAvailableForecastDaysUseCase
import dagger.Module
import dagger.Provides

@Module
class WeatherUseCasesModule {

    @Provides
    fun provideLoadDayWeatherUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): LoadDayWeatherUseCase {
        return LoadDayWeatherUseCase(weatherRepository, errorsHandler)
    }


    @Provides
    fun provideFetchAndSaveAllPlacesForecastUseCase(placesRepository: PlacesRepository, errorsHandler: ErrorsHandler): DailyForecastSyncUseCase {
        return DailyForecastSyncUseCase(placesRepository, errorsHandler)
    }

    @Provides
    fun provideFetchPlaceForecastUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): FetchPlaceForecastUseCase {
        return FetchPlaceForecastUseCase(weatherRepository, errorsHandler)
    }

    @Provides
    fun providePrePopulatePlacesUseCase(placesRepository: PlacesRepository, errorsHandler: ErrorsHandler): PrePopulatePlacesUseCase {
        return PrePopulatePlacesUseCase(placesRepository, errorsHandler)
    }

    @Provides
    fun provideLoadAllPlacesUseCase(placesRepository: PlacesRepository, errorsHandler: ErrorsHandler): GetAllPlacesUse {
        return GetAllPlacesUse(placesRepository, errorsHandler)
    }

    @Provides
    fun provideUpdateCurrentPlaceUseCase(placesRepository: PlacesRepository, weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): UpdateCurrentPlaceUseCase {
        return UpdateCurrentPlaceUseCase(placesRepository, weatherRepository, errorsHandler)
    }

    @Provides
    fun provideLoadCurrentWeatherSingleUseCase(weatherRepository: WeatherRepository): LoadCurrentWeatherSingleUseCase {
        return LoadCurrentWeatherSingleUseCase(weatherRepository)
    }

    @Provides
    fun provideCheckIsDataInitialized(placesRepository: PlacesRepository, errorsHandler: ErrorsHandler): CheckDataInitializedUseCase {
        return CheckDataInitializedUseCase(placesRepository, errorsHandler)
    }

    @Provides
    fun provideDaysLoader(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): LoadPlaceAvailableForecastDaysUseCase {
        return LoadPlaceAvailableForecastDaysUseCase(weatherRepository, errorsHandler)
    }
}