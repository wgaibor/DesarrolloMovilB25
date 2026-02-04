package com.lemas.asistincialemas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lemas.asistincialemas.R;
import com.lemas.asistincialemas.api.ApiClient;
import com.lemas.asistincialemas.model.LoginRequest;
import com.lemas.asistincialemas.model.LoginResponse;
import com.lemas.asistincialemas.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilCedula, tilPassword;
    private TextInputEditText etCedula, etPassword;
    private MaterialButton btnLogin;
    private View progressBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ApiClient.init(this);
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            navigateToRoleActivity();
            return;
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilCedula = findViewById(R.id.tilCedula);
        tilPassword = findViewById(R.id.tilPassword);
        etCedula = findViewById(R.id.etCedula);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        tilCedula.setError(null);
        tilPassword.setError(null);

        String cedula = etCedula.getText() != null ? etCedula.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (cedula.isEmpty()) {
            tilCedula.setError(getString(R.string.error_empty_cedula));
            return;
        }

        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_empty_password));
            return;
        }

        showLoading(true);

        LoginRequest request = new LoginRequest(cedula, password);
        ApiClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    sessionManager.saveSession(loginResponse.getToken(), loginResponse.getUsuario());
                    navigateToRoleActivity();
                } else {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.error_login_failed),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(LoginActivity.this,
                        getString(R.string.error_network) + ": " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToRoleActivity() {
        String rol = sessionManager.getUserRol();
        Intent intent;

        switch (rol) {
            case "ESTUDIANTE":
                intent = new Intent(this, QrActivity.class);
                break;
            case "DOCENTE":
                intent = new Intent(this, EscanerQrActivity.class);
                break;
            case "REPRESENTANTE":
                intent = new Intent(this, HistorialActivity.class);
                break;
            default:
                Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
                return;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }
}
