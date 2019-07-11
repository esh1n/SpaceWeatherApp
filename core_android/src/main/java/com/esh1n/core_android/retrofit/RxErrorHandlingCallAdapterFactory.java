package com.esh1n.core_android.retrofit;



import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.*;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by antonvaliuh on 04.04.17.
 */

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJava2CallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    private static RetrofitException asRetrofitException(Retrofit retrofit, Throwable throwable) {
        // We had non-200 http error
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            Response response = httpException.response();
            String url = response.raw().request().url().toString();
            return RetrofitException.httpError(url, response, retrofit);
        }
        // A network error happened
        if (throwable instanceof IOException) {
            return RetrofitException.networkError((IOException) throwable);
        }

        if (throwable instanceof RetrofitException) {
            return (RetrofitException) throwable;
        }

        // We don't know what happened. We need to simply convert to an unknown error

        return RetrofitException.unexpectedError(throwable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        CallAdapter wrapped = original.get(returnType, annotations, retrofit);

        return new ObservableRxCallAdapterWrapper(retrofit, wrapped);
    }

    private static class ObservableRxCallAdapterWrapper<T, S> implements CallAdapter<T, S> {
        private final Retrofit retrofit;
        private final CallAdapter<T, ?> wrapped;

        public ObservableRxCallAdapterWrapper(Retrofit retrofit, CallAdapter<T, ?> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public S adapt(Call<T> call) {

            Object adapted = wrapped.adapt(call);
            if (adapted instanceof Single) {
                return (S) ((Single<?>) wrapped.adapt(call))
                        .onErrorResumeNext(error ->
                                Single.error(asRetrofitException(retrofit, error)));
            } else if (adapted instanceof Maybe) {
                return (S) ((Maybe<?>) wrapped.adapt(call)).onErrorResumeNext(error -> {
                    return Maybe.error(asRetrofitException(retrofit, error));
                });
            } else if (adapted instanceof Completable) {
                return (S) ((Completable) wrapped.adapt(call)).onErrorResumeNext(error ->
                        Completable.error(asRetrofitException(retrofit, error))
                );
            } else if (adapted instanceof Flowable) {
                return (S) ((Flowable<?>) wrapped.adapt(call)).onErrorResumeNext(error -> {
                            return Flowable.error(asRetrofitException(retrofit, error));
                        }
                );
            } else {
                return (S) ((Observable<?>) adapted).onErrorResumeNext(error -> {
                    return Observable.error(asRetrofitException(retrofit, error));
                });
            }
        }
    }
}
