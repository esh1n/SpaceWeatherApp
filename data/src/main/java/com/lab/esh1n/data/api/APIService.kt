package com.lab.esh1n.data.api

import com.lab.esh1n.data.api.response.WeatherResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by esh1n on 3/9/19.
 */

interface APIService {

    @GET("/data/2.5/weather")
    fun getWeatherAsync(@Query("APPID") appId: String,
                        @Query("q") cityName: String,
                        @Query("units") units: String): Deferred<WeatherResponse>
}