package com.game.simpsonslemas.services;

import com.game.simpsonslemas.models.Personajes;
import com.game.simpsonslemas.models.SimpsonResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class SimpsonApiService {
    private static final String BASE_URL = "https://thesimpsonsapi.com/api/";
    private static SimpsonApiService instance;
    private SimpsonApi api;

    public SimpsonApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(SimpsonApi.class);
    }

    public static SimpsonApiService getInstance() {
        if (instance == null) {
            instance = new SimpsonApiService();
        }
        return instance;
    }

    public SimpsonApi getApi() {
        return api;
    }

    public interface SimpsonApi {
        @GET("characters")
        Call<SimpsonResponse> getPersonajeSimpsons();
    }
}
