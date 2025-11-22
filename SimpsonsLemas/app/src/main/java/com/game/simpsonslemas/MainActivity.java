package com.game.simpsonslemas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.game.simpsonslemas.adapter.SimpsonAdapter;
import com.game.simpsonslemas.models.Personajes;
import com.game.simpsonslemas.models.SimpsonResponse;
import com.game.simpsonslemas.services.SimpsonApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SimpsonApiService apiService;
    RecyclerView rvListPersonaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvListPersonaje = findViewById(R.id.rvPersonajes);
        cargarInformacion();
    }

    private void cargarInformacion() {
        apiService = SimpsonApiService.getInstance();
        apiService.getApi().getPersonajeSimpsons().enqueue(new Callback<SimpsonResponse>() {
            @Override
            public void onResponse(Call<SimpsonResponse> call, Response<SimpsonResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    llenarRecyclerView(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<SimpsonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void llenarRecyclerView(List<Personajes> results) {
        SimpsonAdapter simpsonAdapter = new SimpsonAdapter(results);
        rvListPersonaje.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvListPersonaje.setLayoutManager(gridLayoutManager);
        rvListPersonaje.setAdapter(simpsonAdapter);
    }
}