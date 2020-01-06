package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.*
import com.lab.esh1n.weather.weather.mapper.UILocalizerImpl
import com.lab.esh1n.weather.weather.model.ForecastDayModel
import com.lab.esh1n.weather.weather.model.Temperature
import com.lab.esh1n.weather.weather.model.TemperatureUnit
import com.lab.esh1n.weather.weather.model.Wind
import java.util.*


class DayForecastFragment : Fragment(R.layout.fragment_recyclerview_with_progress) {

    private lateinit var adapter: DayForecastAdapter
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: View? = null
    private var emptyView: View? = null

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        adapter = DayForecastAdapter()
        loadingIndicator = rootView.findViewById(R.id.loading_indicator)
        emptyView = rootView.findViewById(R.id.tv_no_items)
        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(requireActivity())
            it.addItemDecoration(
                    DividerItemDecoration(
                            requireActivity(),
                            DividerItemDecoration.VERTICAL
                    )
            )

            it.setHasFixedSize(true)
            it.adapter = adapter
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dayModel = arguments?.getParcelable<ForecastDayModel>(ARG_PLACE_DAY_FORECAST)
        SnackbarBuilder.buildSnack(requireView(), dayModel?.dayDescription ?: "").show()
        adapter.updateForecastData(prepareFakeDayForecastItems())

    }

    private fun prepareFakeDayForecastItems(): List<Pair<DayForecastSection, List<DaytimeForecastModel>>> {
        val uiLocalizer = UILocalizerImpl { Locale.forLanguageTag(AppPrefs.DEFAULT_LOCALE) }
        val mainItems = arrayListOf(
                DayOverallForecastModel(dayTime = R.string.title_morning, iconId = "01d",
                        temperature = uiLocalizer.localizeTemperature(Temperature(1.0, TemperatureUnit.C))),
                DayOverallForecastModel(dayTime = R.string.title_day, iconId = "02d",
                        temperature = uiLocalizer.localizeTemperature(Temperature(2.0, TemperatureUnit.C))),
                DayOverallForecastModel(dayTime = R.string.title_evening, iconId = "03d",
                        temperature = uiLocalizer.localizeTemperature(Temperature(11.0, TemperatureUnit.C))),
                DayOverallForecastModel(dayTime = R.string.title_night, iconId = "04d",
                        temperature = uiLocalizer.localizeTemperature(Temperature(12.0, TemperatureUnit.C)))
        )
        val mainSection = Pair(DayForecastSection.MAIN, mainItems)
        val windItems = arrayListOf(
                DayWindForecastModel(dayTime = R.string.title_morning, iconId = "wind",
                        wind = uiLocalizer.localizeWind(Wind(1.0, AppPrefs.Units.metric))),
                DayWindForecastModel(dayTime = R.string.title_day, iconId = "wind",
                        wind = uiLocalizer.localizeWind(Wind(5.0, AppPrefs.Units.metric))),
                DayWindForecastModel(dayTime = R.string.title_evening, iconId = "wind",
                        wind = uiLocalizer.localizeWind(Wind(4.0, AppPrefs.Units.metric))),
                DayWindForecastModel(dayTime = R.string.title_night, iconId = "wind",
                        wind = uiLocalizer.localizeWind(Wind(3.0, AppPrefs.Units.metric)))
        )
        val windSection = Pair(DayForecastSection.WIND, windItems)
        return arrayListOf(mainSection, windSection)
    }


    companion object {
        const val ARG_PLACE_DAY_FORECAST = "ARG_PLACE_DAY_FORECAST"
    }
}