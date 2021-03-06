package com.fingertech.kesforstudent.Rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //////////// http://192.168.43.182/apikes/
    public static final String MASTER_URL   = "http://genpin.co.id/";
    public static final String BASE_API     = MASTER_URL + "ztapi2/";
    public static final String BASE_IMAGE   = MASTER_URL + "schoolc/assets/images/profile/mm_";
    public static final String BASE_SILABUS = MASTER_URL + "schoolc/assets/images/silabus/";
    public static final String BASE_LESSON  = MASTER_URL + "schoolc/assets/images/lesson/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}