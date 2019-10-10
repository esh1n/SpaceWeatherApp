package com.lab.esh1n.weather.weather.model

sealed class SettingsModel(val title: String, val value: String)
class TextSettingModel(title: String, value: String) : SettingsModel(title, value)
class HeaderSettingModel(title: String) : SettingsModel(title, "")