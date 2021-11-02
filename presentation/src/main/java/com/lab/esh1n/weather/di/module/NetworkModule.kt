package com.lab.esh1n.weather.di.module

import com.dataart.dartcard.data.retrofit.ApiServiceBuilder
import com.esh1n.core_android.retrofit.OkHttpBuilder
import com.esh1n.core_android.retrofit.RxErrorHandlingCallAdapterFactory
import com.lab.esh1n.weather.BuildConfig
import com.lab.esh1n.weather.data.api.APIService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by esh1n on 3/7/18.
 */

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideUserSessionOkHttpClient(): OkHttpClient {
        //val tokenAuthenticator = TokenAuthenticator(authorizationApiService, cache)
        return OkHttpBuilder.newBuilder(BuildConfig.API_ENDPOINT)
                .withLoggingInterceptor(HttpLoggingInterceptor.Level.BODY)
                .withReadTimeOut(READ_WRITE_TIMEOUT)
                .withWriteTimeOut(READ_WRITE_TIMEOUT)
                .withConnectTimeOut(CONNECT_TIMEOUT)
                .withUnauthorizedErrorInterceptor()
                .withSelfSignedSSLSocketFactory()
                // .withAuthenticator(tokenAuthenticator)
                .build()
    }

    @Provides
    @Singleton
    fun provideUserSessionApiService(client: OkHttpClient): APIService {
        return ApiServiceBuilder(endPointUrl = BuildConfig.API_ENDPOINT,
                client = client,
                converterFactory = GsonConverterFactory.create(),
                callAdapterFactory = RxErrorHandlingCallAdapterFactory.create())
                .build(APIService::class.java)
    }

    companion object {
        private const val READ_WRITE_TIMEOUT = 60
        private const val CONNECT_TIMEOUT = 30
    }

}