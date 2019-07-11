package com.esh1n.core_android.retrofit;

import retrofit2.Response;
import retrofit2.Retrofit;

public class RefreshTokenException extends RetrofitException {
    public RefreshTokenException(String message, String url, Response response, Kind kind, Throwable exception, Retrofit retrofit) {
        super(message, url, response, kind, exception, retrofit);
    }
}
