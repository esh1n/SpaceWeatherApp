package com.esh1n.core_android.retrofit

import android.util.Log
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.net.URI
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class OkHttpBuilder private constructor(endPoint: String) {
    private val mHostName: String = URI.create(endPoint).host
    private val mClientBuilder = OkHttpClient.Builder()

    fun withLoggingInterceptor(level: HttpLoggingInterceptor.Level): OkHttpBuilder {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = level
        mClientBuilder.addInterceptor(loggingInterceptor)
        mClientBuilder.protocols(listOf(Protocol.HTTP_1_1))
        return this
    }

    fun withReadTimeOut(timeInSeconds: Int): OkHttpBuilder {
        mClientBuilder.readTimeout(timeInSeconds.toLong(), TimeUnit.SECONDS)
        return this
    }

    fun withWriteTimeOut(timeInSeconds: Int): OkHttpBuilder {
        mClientBuilder.writeTimeout(timeInSeconds.toLong(), TimeUnit.SECONDS)
        return this
    }

    fun withConnectTimeOut(timeInSeconds: Int): OkHttpBuilder {
        mClientBuilder.connectTimeout(timeInSeconds.toLong(), TimeUnit.SECONDS)
        return this
    }


    fun withClientTypeInterceptor(): OkHttpBuilder {
        mClientBuilder.addInterceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
            builder.header("Client-Type", "Android")
            chain.proceed(builder.build())
        }

        return this
    }

    fun withAuthenticator(authenticator: Authenticator): OkHttpBuilder {
        mClientBuilder.authenticator(authenticator)
        return this
    }


    fun withUnauthorizedErrorInterceptor(): OkHttpBuilder {
        mClientBuilder.addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code() == UNAUTHORIZED_HTTP_CODE) {

                //rxBus.postEvent(new UnauthorizedErrorEvent());
            }
            response
        }

        return this
    }

    fun withSelfSignedSSLSocketFactory(): OkHttpBuilder {
        try {
            val sslContext = SSLContext.getInstance("SSL")
            val trustManager = object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
            sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            mClientBuilder.sslSocketFactory(sslSocketFactory, trustManager)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to setup self signed ssl socket factory", e)
        } catch (e: KeyManagementException) {
            throw RuntimeException("Failed to setup self signed ssl socket factory", e)
        }

        return this
    }


    fun withHostNameVerifier(): OkHttpBuilder {
        mClientBuilder.hostnameVerifier { hostname, _ -> hostname.equals(mHostName, ignoreCase = true) }
        return this
    }

    fun build(): OkHttpClient {
        return mClientBuilder.build()
    }

    private fun addAuthorizationHeader(requestBuilder: Request.Builder, token: String?): Request.Builder {
        Log.d("Auth", "attach token to request $token")
        requestBuilder.header("X-Auth-Token", token)
        return requestBuilder
    }

    companion object {
        private const val UNAUTHORIZED_HTTP_CODE = 401

        fun newBuilder(endPoint: String): OkHttpBuilder {
            return OkHttpBuilder(endPoint)
        }
    }
}