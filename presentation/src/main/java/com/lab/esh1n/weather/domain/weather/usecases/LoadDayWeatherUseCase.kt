package com.lab.esh1n.weather.domain.weather.usecases

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import io.reactivex.Single
import java.util.*


class LoadDayWeatherUseCase(private val weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler)
    : UseCase<Single<Resource<List<WeatherWithPlace>>>, PlaceDayArgs>(errorsHandler) {
    override fun perform(args: PlaceDayArgs): Single<Resource<List<WeatherWithPlace>>> {
        return weatherRepository.getWeathersForPlaceAtDay(args.placeId, args.dayDate, args.timezone)
                .map { days -> Resource.success(days) }
                .onErrorReturn { error -> Resource.error(errorsHandler.handle(error)) }
    }

}

class PlaceDayArgs(val placeId: Int, val dayDate: Date, val timezone: String) {
}