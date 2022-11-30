package com.dev334.trace.util.retrofit;

import android.content.Context;
import android.util.Log;

import com.dev334.trace.util.app.AppConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://172.20.10.3:8000/";
    private static Retrofit retrofit=null;
    private static String auth_token="";

    public static Retrofit getApiClient(Context context){
        if(retrofit==null){
            Gson gson=new GsonBuilder().setLenient().create();
            AppConfig appConfig=new AppConfig(context);
            Log.i("AuthTokenSaved", "getApiClient: "+appConfig.getAuthToken());
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(
                            new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request original = chain.request();

                                    // Request customization: add request headers
                                    Request.Builder requestBuilder = original.newBuilder()
                                            .header("auth_token", appConfig.getAuthToken())
                                            .method(original.method(), original.body());

                                    Request request = requestBuilder.build();
                                    return chain.proceed(request);
                                }
                            })
                    .addInterceptor(new NetworkConnectionInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
            return retrofit;
        }

        return retrofit;
    }

}
