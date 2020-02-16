package com.lab.esh1n.weather.domain.weather.weather.usecases

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.weather.WeatherRepository
import io.reactivex.Flowable
import java.util.*

class LoadPlaceAvailableForecastDaysUseCase(private val weatherRepository: WeatherRepository,
                                            errorsHandler: ErrorsHandler) : UseCase<Flowable<Resource<AvailableDaysResult>>, Args>(errorsHandler) {
    override fun perform(args: Args): Flowable<Resource<AvailableDaysResult>> {
        return weatherRepository.getAvailableDaysForPlace(args.placeId)
                .map { days ->
                    val timeZone = days.second
                    val selectedDateIndex = days.third.indexOfFirst { DateBuilder(it, timeZone).isSameDay(args.selectedDate) }
                    Resource.success(AvailableDaysResult(placeId = days.first,
                            timezone = days.second,
                            dates = days.third,
                            selectedDateIndex = selectedDateIndex))
                }
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }
}

class Args(val placeId: Int, val selectedDate: Int)
class AvailableDaysResult(val placeId: Int, val timezone: String, val dates: List<Date>, val selectedDateIndex: Int)