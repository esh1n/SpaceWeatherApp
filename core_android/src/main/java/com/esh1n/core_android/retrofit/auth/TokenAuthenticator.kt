package com.esh1n.core_android.retrofit.auth

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(private val apiService: AuthService, private val tokenProvider: TokenProvider) :
    Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(TokenAuthenticator::class.java) {
            val oldToken = tokenProvider.token
            if (response.code() == 401 && oldToken != null) {
                val refreshResponse = apiService.refreshToken(oldToken).blockingGet()
                if (refreshResponse != null) {
                    val token = refreshResponse.token
                    tokenProvider.token = token
                    return response.request().newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    return null
                }
            } else {
                return response.request()
                    .newBuilder()
                    .header("Authorization", "Bearer $oldToken")
                    .build()
            }
        }
    }

}