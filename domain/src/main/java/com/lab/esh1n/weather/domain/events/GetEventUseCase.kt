package com.lab.esh1n.weather.domain.events

import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.base.Resource
import io.reactivex.Flowable

class GetEventUseCase(private val weatherDAO: WeatherDAO, private val errorsHandler: ErrorsHandler)
    : UseCase<Long, Flowable<Resource<WeatherEntity>>> {
    override fun execute(argument: Long): Flowable<Resource<WeatherEntity>> {
        return weatherDAO.getEventById(argument)
                .map { events -> Resource.success(events) }
                .onErrorReturn { error -> Resource.error(errorsHandler.handle(error)) }
    }
}
