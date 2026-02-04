package com.lemas.asistincialemas.api;

import android.content.Context;

import com.lemas.asistincialemas.util.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://192.168.100.10:8080/";

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private static SessionManager sessionManager = null;

    public static void init(Context context) {
        sessionManager = new SessionManager(context);
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    private static class AuthInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            if (sessionManager != null && sessionManager.getToken() != null) {
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + sessionManager.getToken())
                        .method(original.method(), original.body());

                return chain.proceed(requestBuilder.build());
            }

            return chain.proceed(original);
        }
    }

    public static void setBaseUrl(String url) {
        apiService = null;
        retrofit = null;
    }
}
