package com.example.pokemonapp.service;

import com.example.pokemonapp.model.Pokemon;
import com.example.pokemonapp.model.PokemonListResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class PokemonApiService {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private static PokemonApiService instance;
    private PokemonApi api;

    private PokemonApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(PokemonApi.class);
    }

    public static PokemonApiService getInstance() {
        if (instance == null) {
            instance = new PokemonApiService();
        }
        return instance;
    }

    public PokemonApi getApi() {
        return api;
    }

    public interface PokemonApi {
        @GET("pokemon")
        Call<PokemonListResponse> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);

        @GET("pokemon/{id}")
        Call<Pokemon> getPokemonById(@Path("id") int id);

        @GET("pokemon/{name}")
        Call<Pokemon> getPokemonByName(@Path("name") String name);
    }
}
