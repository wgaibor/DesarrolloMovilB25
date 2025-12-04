package com.game.simpsonslemas;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.game.simpsonslemas.adapter.SimpsonAdapter;
import com.game.simpsonslemas.models.Personajes;
import com.game.simpsonslemas.models.SimpsonResponse;
import com.game.simpsonslemas.services.SimpsonApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SimpsonApiService apiService;
    RecyclerView rvListPersonaje;

    GridLayoutManager gridLayoutManager;

    List<Personajes> lstPersonajes = null;
    SimpsonAdapter simpsonAdapter;
    private boolean estaCargando = false;
    private int paginaActual = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvListPersonaje = findViewById(R.id.rvPersonajes);
        gridLayoutManager = new GridLayoutManager(this, 2);
        lstPersonajes = new ArrayList<>();
        llenarRecyclerView(lstPersonajes);
        configurarScrollListener();
        cargarInformacion(paginaActual);
    }

    private void configurarScrollListener() {
        rvListPersonaje.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totatItems = gridLayoutManager.getItemCount();
                int ultimoItemVisible = gridLayoutManager.findLastVisibleItemPosition();
                // Si estamos cerca del final del recyclerview (último 5 elementos) y no se esta consumiendo ws
                if (!estaCargando && ultimoItemVisible >=  totatItems - 5) {
                    cargarSiguientePagina();
                    Log.i("PAGINA ACTUAL", paginaActual+"");
                }
            }
        });
    }

    private void cargarSiguientePagina() {
        if (!estaCargando) {
            paginaActual++;
            cargarInformacion(paginaActual);
        }
    }

    private void cargarInformacion(int paginaActual) {
        apiService = SimpsonApiService.getInstance();
        estaCargando = true;
        apiService.getApi().getPersonajeSimpsonsByPage(paginaActual).enqueue(new Callback<SimpsonResponse>() {
            @Override
            public void onResponse(Call<SimpsonResponse> call, Response<SimpsonResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    List<Personajes> lstPersonajeSimpson = response.body().getResults();
                    if (paginaActual == 1) {
                        lstPersonajes.clear();
                        lstPersonajes.addAll(lstPersonajeSimpson);
                        simpsonAdapter.notifyDataSetChanged();
                    } else {
                        simpsonAdapter.agregarPersonajes(lstPersonajeSimpson);
                    }
                }
                estaCargando = false;
            }

            @Override
            public void onFailure(Call<SimpsonResponse> call, Throwable t) {
                t.printStackTrace();
                estaCargando = false;
            }
        });
    }

    private void llenarRecyclerView(List<Personajes> results) {
        simpsonAdapter = new SimpsonAdapter(this, results);
        rvListPersonaje.setHasFixedSize(true);
        rvListPersonaje.setLayoutManager(gridLayoutManager);
        rvListPersonaje.setAdapter(simpsonAdapter);
    }
}