package com.lemas.asistincialemas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.lemas.asistincialemas.R;
import com.lemas.asistincialemas.adapter.AsistenciaAdapter;
import com.lemas.asistincialemas.api.ApiClient;
import com.lemas.asistincialemas.model.AsistenciaResponse;
import com.lemas.asistincialemas.model.EstudianteResponse;
import com.lemas.asistincialemas.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialActivity extends AppCompatActivity {

    private TextView tvWelcome, tvEmpty;
    private TabLayout tabLayout;
    private RecyclerView rvAsistencias;
    private ProgressBar progressBar;
    private MaterialButton btnLogout;
    private SessionManager sessionManager;
    private AsistenciaAdapter adapter;
    private List<EstudianteResponse> estudiantes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        sessionManager = new SessionManager(this);
        ApiClient.init(this);

        initViews();
        setupRecyclerView();
        setupListeners();
        loadEstudiantes();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvEmpty = findViewById(R.id.tvEmpty);
        tabLayout = findViewById(R.id.tabLayout);
        rvAsistencias = findViewById(R.id.rvAsistencias);
        progressBar = findViewById(R.id.progressBar);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText(getString(R.string.welcome_message, sessionManager.getUserNombre()));
    }

    private void setupRecyclerView() {
        adapter = new AsistenciaAdapter(this);
        rvAsistencias.setLayoutManager(new LinearLayoutManager(this));
        rvAsistencias.setAdapter(adapter);
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> logout());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position < estudiantes.size()) {
                    loadAsistencias(estudiantes.get(position).getId());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadEstudiantes() {
        showLoading(true);

        ApiClient.getApiService().obtenerMisEstudiantes().enqueue(new Callback<List<EstudianteResponse>>() {
            @Override
            public void onResponse(Call<List<EstudianteResponse>> call,
                                   Response<List<EstudianteResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    estudiantes = response.body();
                    setupTabs();

                    if (!estudiantes.isEmpty()) {
                        loadAsistencias(estudiantes.get(0).getId());
                    } else {
                        showLoading(false);
                        showEmpty(true);
                    }
                } else {
                    showLoading(false);
                    Toast.makeText(HistorialActivity.this,
                            getString(R.string.error_network),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EstudianteResponse>> call, Throwable t) {
                showLoading(false);
                Toast.makeText(HistorialActivity.this,
                        getString(R.string.error_network) + ": " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupTabs() {
        tabLayout.removeAllTabs();
        for (EstudianteResponse estudiante : estudiantes) {
            String tabTitle = estudiante.getNombre();
            if (estudiante.getParentesco() != null) {
                tabTitle += " (" + estudiante.getParentesco() + ")";
            }
            tabLayout.addTab(tabLayout.newTab().setText(tabTitle));
        }
    }

    private void loadAsistencias(Long estudianteId) {
        showLoading(true);
        showEmpty(false);

        ApiClient.getApiService().obtenerAsistenciasEstudiante(estudianteId, null, null, null)
                .enqueue(new Callback<List<AsistenciaResponse>>() {
                    @Override
                    public void onResponse(Call<List<AsistenciaResponse>> call,
                                           Response<List<AsistenciaResponse>> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            List<AsistenciaResponse> asistencias = response.body();
                            adapter.setAsistencias(asistencias);
                            showEmpty(asistencias.isEmpty());
                        } else {
                            showEmpty(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AsistenciaResponse>> call, Throwable t) {
                        showLoading(false);
                        showEmpty(true);
                        Toast.makeText(HistorialActivity.this,
                                getString(R.string.error_network),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmpty(boolean show) {
        tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        rvAsistencias.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void logout() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
