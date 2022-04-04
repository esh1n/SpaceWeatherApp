package com.lab.esh1n.weather.utils

import androidx.recyclerview.widget.RecyclerView

inline fun <reified T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> RecyclerView.adapter() =
    adapter as T