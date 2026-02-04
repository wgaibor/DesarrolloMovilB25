package com.lemas.asistincialemas.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lemas.asistincialemas.R;
import com.lemas.asistincialemas.api.ApiClient;
import com.lemas.asistincialemas.model.QrResponse;
import com.lemas.asistincialemas.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrActivity extends AppCompatActivity {

    private TextView tvWelcome, tvCurso;
    private ImageView ivQrCode;
    private ProgressBar progressBar;
    private MaterialButton btnLogout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        sessionManager = new SessionManager(this);
        ApiClient.init(this);

        initViews();
        setupListeners();
        loadQrCode();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvCurso = findViewById(R.id.tvCurso);
        ivQrCode = findViewById(R.id.ivQrCode);
        progressBar = findViewById(R.id.progressBar);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText(getString(R.string.welcome_message, sessionManager.getUserNombre()));
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadQrCode() {
        showLoading(true);

        ApiClient.getApiService().obtenerMiQr().enqueue(new Callback<QrResponse>() {
            @Override
            public void onResponse(Call<QrResponse> call, Response<QrResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    QrResponse qrResponse = response.body();
                    tvCurso.setText(qrResponse.getCurso());
                    generateQrCode(qrResponse.getCodigoQr());
                } else {
                    Toast.makeText(QrActivity.this,
                            getString(R.string.error_network),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QrResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(QrActivity.this,
                        getString(R.string.error_network) + ": " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateQrCode(String content) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            ivQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "Error generando QR", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        ivQrCode.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void logout() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
