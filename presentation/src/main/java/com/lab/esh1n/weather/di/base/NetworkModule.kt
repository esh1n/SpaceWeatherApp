package com.lab.esh1n.weather.di.base

import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.api.RxErrorHandlingCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by esh1n on 3/7/18.
 */

@Module
class NetworkModule {
    companion object {
        private const val URL = "https://api.github.com/"
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideAPIService(client: OkHttpClient, converterFactory: Converter.Factory): APIService {
        return Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build()
                .create(APIService::class.java)

    }

}