package com.lab.esh1n.weather.weather.model

sealed class SettingsModel(val title: String)
class TextSettingModel(title: String, val value: String) : SettingsModel(title)
class HeaderSettingModel(title: String) : SettingsModel(title)