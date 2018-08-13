package com.example.akash.proofofconcept.networkUtils;

import android.support.test.espresso.IdlingResource;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.jakewharton.espresso.OkHttp3IdlingResource;

public class ApiClient {
    private OkHttpClient okHttpClient;
    private static ApiClient INSTANCE= null;
    private Retrofit retrofit;
    private static IdlingResource mResource;


    private ApiClient(){
        String COUNTRY_BASE_URL= "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/";
        if(okHttpClient == null)
            initOkHttp();

        //Building retrofit
        Retrofit.Builder retrofitBuilder= new Retrofit.Builder()
                .baseUrl(COUNTRY_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofit=  retrofitBuilder.build();
    }

    public synchronized static ApiClient getInstance(){
        if(INSTANCE == null){
            INSTANCE= new ApiClient();
        }
        return INSTANCE;
    }

    public  Retrofit getClient(){
        return retrofit;
    }

    //Initializing okhttp
    private void initOkHttp(){
        long REQUEST_TIME_OUT= 60;
        OkHttpClient.Builder builder= new OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor httpLoggingInterceptor= new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(httpLoggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest= chain.request();
                Request.Builder requestBuilder= originalRequest.newBuilder();
                requestBuilder.addHeader("Accept", "application/json")
                        .addHeader("Request-Type", "Android")
                        .addHeader("Content-Type", "application/json");
                return chain.proceed(requestBuilder.build());
            }
        });
        okHttpClient= builder.build();
        mResource= OkHttp3IdlingResource.create("OkHttp", okHttpClient);
    }
    public static IdlingResource getmResource(){
        return mResource;
    }
}
