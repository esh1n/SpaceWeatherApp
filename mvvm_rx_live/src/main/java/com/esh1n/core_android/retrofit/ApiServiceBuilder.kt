package com.dataart.dartcard.data.retrofit


import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit


class ApiServiceBuilder constructor(private val endPointUrl: String,
                                            private val converterFactory: Converter.Factory,
                                            private val callAdapterFactory: CallAdapter.Factory,
                                            private val client: OkHttpClient) {
    fun <S> build(clazz: Class<S>): S {
        return Retrofit.Builder()
                .baseUrl(endPointUrl)
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
                .create(clazz)
    }

}