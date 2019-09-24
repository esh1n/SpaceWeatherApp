package com.lab.esh1n.weather.weather.model

data class PlaceModel(val id: Int,
                      val name: String,
                      val iconId: String,
                      val time: String,
                      val temperature: Int,
                      val weatherBackgroundModel: WeatherBackgroundModel)