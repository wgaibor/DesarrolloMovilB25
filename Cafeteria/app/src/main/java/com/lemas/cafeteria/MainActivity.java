package com.lemas.cafeteria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.lemas.cafeteria.activity.CreateLoginActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtCorreo;
    EditText edtConstrasena;
    TextView tvRegistrarse;
    Button btnIngresar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth          = FirebaseAuth.getInstance();
        
        edtCorreo      = findViewById(R.id.edt_email_sesion);
        edtConstrasena = findViewById(R.id.edt_contrasena_sesion);
        tvRegistrarse  = findViewById(R.id.tv_registrate_sesion);
        btnIngresar    = findViewById(R.id.btn_ingresar_sesion);

        tvRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(MainActivity.this, CreateLoginActivity.class);
                startActivity(intento);
                finish();
            }
        });
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCredenciales();
            }
        });
    }

    private void validarCredenciales() {
    }
}