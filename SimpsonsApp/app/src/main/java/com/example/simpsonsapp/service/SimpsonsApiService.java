package com.example.simpsonsapp.service;

import com.example.simpsonsapp.model.Character;
import com.example.simpsonsapp.model.CharacterListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface SimpsonsApiService {
    String BASE_URL = "https://thesimpsonsapi.com/api/";
    
    @GET("characters")
    Call<CharacterListResponse> getCharacters(@Query("page") int page);
    
    @GET("characters/{id}")
    Call<Character> getCharacterById(@Path("id") int id);
}

