package com.lab.esh1n.weather.di.weather

import com.esh1n.core_android.error.ErrorsHandler
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.weather.domain.weather.places.PlacesRepository
import com.lab.esh1n.weather.domain.weather.places.usecase.*
import com.lab.esh1n.weather.domain.weather.settings.LoadSettingsUseCase
import com.lab.esh1n.weather.domain.weather.weather.WeatherRepository
import com.lab.esh1n.weather.domain.weather.weather.usecases.*
import dagger.Module
import dagger.Provides

@Module
class WeatherUseCasesModule {

    @Provides
    fun provideLoadDayWeatherUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): LoadDayWeatherUseCase {
        return LoadDayWeatherUseCase(weatherRepository, errorsHandler)
    }

    @Provides
    fun provideFetchAndSaveCurrentPlaceWeatherUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): FetchAndSaveCurrentPlaceWeatherUseCase {
        return FetchAndSaveCurrentPlaceWeatherUseCase(weatherRepository, errorsHandler)
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
    fun provideLoadSettingsUseCase(prefs: AppPrefs, errorsHandler: ErrorsHandler): LoadSettingsUseCase {
        return LoadSettingsUseCase(prefs, errorsHandler)
    }

    @Provides
    fun providePrePopulatePlacesUseCase(placesRepository: PlacesRepository, errorsHandler: ErrorsHandler): PrePopulatePlacesUseCase {
        return PrePopulatePlacesUseCase(placesRepository, errorsHandler)
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


    @Provides
    fun provideLoadCurrentWeatherSingleUseCase(weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler): LoadCurrentWeatherSingleUseCase {
        return LoadCurrentWeatherSingleUseCase(weatherRepository, errorsHandler)
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