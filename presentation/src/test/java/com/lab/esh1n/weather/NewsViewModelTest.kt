package com.lab.esh1n.weather


import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.esh1n.core_android.ui.viewmodel.Resource
import com.google.common.truth.Truth
import com.lab.esh1n.data.cache.entity.SunsetSunrisePlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.weather.usecases.FetchAndSaveCurrentPlaceWeatherUseCase
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadCurrentWeatherSingleUseCase
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadCurrentWeatherUseCase
import com.lab.esh1n.weather.utils.RxImmediateSchedulerRule
import com.lab.esh1n.weather.utils.getOrAwaitValue
import com.lab.esh1n.weather.weather.mapper.UiLocalizer
import com.lab.esh1n.weather.weather.mapper.WeatherModelMapper
import com.lab.esh1n.weather.weather.model.DayWeatherModel
import com.lab.esh1n.weather.weather.viewmodel.CurrentWeatherVM
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class NewsViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @MockK
    lateinit var loadCurrentWeatherUseCase: LoadCurrentWeatherUseCase

    @MockK
    lateinit var loadCurrentWeatherSingleUseCase: LoadCurrentWeatherSingleUseCase

    @MockK
    lateinit var fetchAndSaveCurrentPlaceWeatherUseCase: FetchAndSaveCurrentPlaceWeatherUseCase

    @MockK
    lateinit var app: Application

    private lateinit var viewModel: CurrentWeatherVM


    @MockK
    lateinit var localizer: UiLocalizer


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkConstructor(WeatherModelMapper::class)
        viewModel = CurrentWeatherVM(loadCurrentWeatherUseCase, loadCurrentWeatherSingleUseCase,
                fetchAndSaveCurrentPlaceWeatherUseCase, localizer, app)

    }

    @Test
    fun testApiFetchDataSuccess() {
        // Given
        val res = createResource()
        val resourceObservable = Observable.just(res)
        every { anyConstructed<WeatherModelMapper>().map(any()) } returns arrayListOf(DayWeatherModel("day", "weather", "weather", 14, 14))

        every { loadCurrentWeatherUseCase.perform(Unit) } returns resourceObservable

        // When
        viewModel.loadWeather()

        //Then
        verify(exactly = 1) { viewModel.mapWeatherDataResource(res) }
        verify(exactly = 1) { res.data }
        val mapResult = WeatherModelMapper(localizer).map(Pair(SunsetSunrisePlaceEntry.createEmpty(), arrayListOf()))
        Truth.assertThat(mapResult.isNotEmpty()).isTrue()
        //Truth.assertThat(weatherModelMapper.map(any()).isEmpty()).isTrue()
        val result = viewModel.getWeatherLiveData().getOrAwaitValue()
        Truth.assertThat(result.status).isEqualTo(Resource.Status.SUCCESS)
        Truth.assertThat(result.status).isNotEqualTo(Resource.Status.ERROR)
        verify(exactly = 1) { loadCurrentWeatherUseCase.perform(any()) }
        confirmVerified(loadCurrentWeatherUseCase)
    }

    private fun createResource(): Resource<Pair<SunsetSunrisePlaceEntry, List<WeatherWithPlace>>> {
        val placeEntry = SunsetSunrisePlaceEntry(1, Date(), Date())
        val weatherWithPlaces: List<WeatherWithPlace> = ArrayList()
        return spyk(Resource(Resource.Status.SUCCESS, Pair(placeEntry, weatherWithPlaces), null))
    }
}

