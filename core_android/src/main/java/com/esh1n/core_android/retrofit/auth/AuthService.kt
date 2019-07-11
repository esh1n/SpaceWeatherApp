package com.esh1n.core_android.retrofit.auth

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService {

    @GET("/disco/google/token")
    fun getToken(@Query("authCode") login: String, password:String): Single<TokenProvider>

    @GET("/disco_dev/google/refresh")
    fun refreshToken(@Query("accessToken") accessToken: String): Single<TokenProvider>
}