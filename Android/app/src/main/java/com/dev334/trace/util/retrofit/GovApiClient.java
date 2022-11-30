package com.dev334.trace.util.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GovApiClient {
    private static final String BASE_URL = "https://api.data.gov.in/";
    private static Retrofit retrofit=null;

    public static Retrofit getApiClient(Context context){
        if(retrofit==null){
            Gson gson=new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit;
        }

        return retrofit;
    }

}

