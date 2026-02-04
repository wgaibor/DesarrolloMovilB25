package com.lemas.asistincialemas.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.lemas.asistincialemas.R;
import com.lemas.asistincialemas.api.ApiClient;
import com.lemas.asistincialemas.model.AsistenciaResponse;
import com.lemas.asistincialemas.model.RegistrarAsistenciaRequest;
import com.lemas.asistincialemas.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EscanerQrActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int SCAN_DELAY_MS = 3000;

    private DecoratedBarcodeView barcodeScanner;
    private TextView tvWelcome, tvResultTitle, tvResultName, tvResultStatus;
    private ProgressBar progressBar;
    private MaterialButton btnLogout;
    private SessionManager sessionManager;
    private Handler handler;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner_qr);

        sessionManager = new SessionManager(this);
        ApiClient.init(this);
        handler = new Handler(Looper.getMainLooper());

        initViews();
        setupListeners();
        checkCameraPermission();
    }

    private void initViews() {
        barcodeScanner = findViewById(R.id.barcodeScanner);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvResultTitle = findViewById(R.id.tvResultTitle);
        tvResultName = findViewById(R.id.tvResultName);
        tvResultStatus = findViewById(R.id.tvResultStatus);
        progressBar = findViewById(R.id.progressBar);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText(getString(R.string.welcome_message, sessionManager.getUserNombre()));
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> logout());

        barcodeScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null && result.getText() != null && !isProcessing) {
                    isProcessing = true;
                    String qrCode = result.getText();
                    registrarAsistencia(qrCode);
                }
            }
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        } else {
            barcodeScanner.resume();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeScanner.resume();
            } else {
                Toast.makeText(this, "Se requiere permiso de cámara", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void registrarAsistencia(String codigoQr) {
        showLoading(true);
        resetResult();

        RegistrarAsistenciaRequest request = new RegistrarAsistenciaRequest(codigoQr);

        ApiClient.getApiService().registrarAsistencia(request).enqueue(new Callback<AsistenciaResponse>() {
            @Override
            public void onResponse(Call<AsistenciaResponse> call, Response<AsistenciaResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AsistenciaResponse asistencia = response.body();
                    showResult(asistencia);
                } else {
                    showError(getString(R.string.error_escaneo));
                }

                scheduleResumeScanning();
            }

            @Override
            public void onFailure(Call<AsistenciaResponse> call, Throwable t) {
                showLoading(false);
                showError(getString(R.string.error_network));
                scheduleResumeScanning();
            }
        });
    }

    private void showResult(AsistenciaResponse asistencia) {
        String nombre = asistencia.getEstudiante() != null ?
                asistencia.getEstudiante().getNombre() : "Estudiante";
        String estado = asistencia.getEstadoAsistencia();

        tvResultTitle.setText(getString(R.string.asistencia_registrada));
        tvResultName.setText(nombre);
        tvResultName.setVisibility(View.VISIBLE);

        tvResultStatus.setText(getEstadoText(estado));
        tvResultStatus.setTextColor(getEstadoColor(estado));
        tvResultStatus.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        tvResultTitle.setText(message);
        tvResultName.setVisibility(View.GONE);
        tvResultStatus.setVisibility(View.GONE);
    }

    private void resetResult() {
        tvResultTitle.setText(getString(R.string.escaner_instruction));
        tvResultName.setVisibility(View.GONE);
        tvResultStatus.setVisibility(View.GONE);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void scheduleResumeScanning() {
        handler.postDelayed(() -> isProcessing = false, SCAN_DELAY_MS);
    }

    private String getEstadoText(String estado) {
        switch (estado) {
            case "PRESENTE":
                return getString(R.string.estado_presente);
            case "ATRASO":
                return getString(R.string.estado_atraso);
            case "FALTA":
                return getString(R.string.estado_falta);
            default:
                return estado;
        }
    }

    private int getEstadoColor(String estado) {
        switch (estado) {
            case "PRESENTE":
                return ContextCompat.getColor(this, R.color.status_presente);
            case "ATRASO":
                return ContextCompat.getColor(this, R.color.status_atraso);
            case "FALTA":
                return ContextCompat.getColor(this, R.color.status_falta);
            default:
                return ContextCompat.getColor(this, R.color.black);
        }
    }

    private void logout() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            barcodeScanner.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScanner.pause();
    }
}
