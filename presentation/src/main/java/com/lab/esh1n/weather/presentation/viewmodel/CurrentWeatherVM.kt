package com.lab.esh1n.weather.presentation.viewmodel

import android.util.Log
import com.esh1n.core_android.common.*
import com.esh1n.core_android.ui.viewmodel.AutoClearViewModel
import com.lab.esh1n.weather.data.cache.entity.SunsetSunriseTimezonePlaceEntry
import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import com.lab.esh1n.weather.presentation.currentplace.OpenForecastArgs
import com.lab.esh1n.weather.presentation.mapper.WeatherModelMapper
import com.lab.esh1n.weather.presentation.model.CurrentWeatherModel
import com.lab.esh1n.weather.presentation.model.WeatherModel
import com.worka.android.app.common.livedata.LiveDataFactory
import io.reactivex.Observable
import javax.inject.Inject


/**
 * Created by esh1n on 3/16/18.
 */

class CurrentWeatherVM
@Inject
constructor(
    private val weatherRepository: WeatherRepository,
    prefsInteractor: IPrefsInteractor, uiLocalisator: IUILocalisator
) : AutoClearViewModel() {

    val refreshEffect = LiveDataFactory.mutableEvent<Result<Unit>>()
    val currentWeatherUiState = LiveDataFactory.mutable<Result<CurrentWeatherUiState>>(Loading())
    val openForecastEvent = LiveDataFactory.mutableEvent<OpenForecastArgs>()
    private val cityWeatherModelMapper = WeatherModelMapper(uiLocalisator, prefsInteractor)

    fun loadWeather() {
        //think about if no results how not to show progress
        Observable.combineLatest(
            weatherRepository.getCurrentWeatherWithForecast(),
            weatherRepository.getCurrentPlaceSunsetAndSunrise()
        ) { weathers, sunset -> Pair(sunset, weathers) }
            .map(::mapWeatherDataResource)
            .catchError()
            .async()
            .subscribeOnResult(
                onNext = currentWeatherUiState::setValue,
                onError = { Log.d("CurrentWeatherVM", it.message) })
            .disposeOnDestroy()
    }

    //TODO refactor mapping to mapper with different values instead list of everything
    private fun mapWeatherDataResource(arg: Pair<SunsetSunriseTimezonePlaceEntry, List<WeatherWithPlace>>): CurrentWeatherUiState {
        val (sunsetSunrise, weathers) = arg
        val weatherModels = if (weathers.isNotEmpty()) cityWeatherModelMapper.map(
            sunsetSunrise,
            weathers
        ) else emptyList()
        val placeName = if (weatherModels.isEmpty()) "" else getPlaceName(weatherModels[0])
        return CurrentWeatherUiState(sunsetSunrise.id, placeName, weatherModels)
    }


    private fun getPlaceName(weatherModel: WeatherModel) =
        (weatherModel as? CurrentWeatherModel)?.placeName ?: ""

    fun refresh() {
        weatherRepository.fetchAndSaveAllPlacesCurrentWeathers()
            .doOnComplete {
                //TODO move Notification util to DI
                //NotificationUtil.sendCurrentWeatherNotification(it, getApplication(), uiLocalizer)
            }
            .doOnSubscribe { refreshEffect.postValue(Loading()) }
            .async()
            .subscribeOnError(
                onComplete = { refreshEffect.value = Success(Unit) },
                onError = { refreshEffect.value = Failure(it) })
            .disposeOnDestroy()
    }

    fun openForecast(dayOfTheYear: Int) {
        currentWeatherUiState.value?.data?.let { state ->
            val (placeId, title, _) = state
            openForecastEvent.value = OpenForecastArgs(placeId, title, dayOfTheYear)
        }
    }
}

data class CurrentWeatherUiState(
    val placeId: Int = -1,
    val title: String = "",
    val weatherModels: List<WeatherModel> = emptyList()
)