package com.lab.esh1n.weather.data.api

import com.lab.esh1n.weather.data.api.response.ForecastResponse
import com.lab.esh1n.weather.data.api.response.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by esh1n on 3/9/19.
 */

interface APIService {

    @GET("/data/2.5/weather")
    fun getWeatherAsync(@Query("APPID") appId: String,
                        @Query("id") id: Int,
                        @Query("lang") lang: String,
                        @Query("units") units: String): Single<WeatherResponse>

    @GET("/data/2.5/forecast")
    fun getForecastAsync(@Query("APPID") appId: String,
                         @Query("id") id: Int,
                         @Query("lang") lang: String,
                         @Query("units") units: String): Single<ForecastResponse>
}