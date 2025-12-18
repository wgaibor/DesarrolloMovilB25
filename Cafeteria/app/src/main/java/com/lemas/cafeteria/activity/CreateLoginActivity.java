package com.lemas.cafeteria.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lemas.cafeteria.MainActivity;
import com.lemas.cafeteria.R;

public class CreateLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText edtCorreo;
    EditText edtContrasena;
    Button btnGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login);
        mAuth         = FirebaseAuth.getInstance();
        edtCorreo     = findViewById(R.id.edt_email);
        edtContrasena = findViewById(R.id.edt_contrasena);
        btnGuardar    = findViewById(R.id.btn_guardar_login);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser() {
        mAuth.createUserWithEmailAndPassword(edtCorreo.getText().toString(), edtContrasena.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        creado();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void creado() {
        mostrarAlerta("Se ha registrado el usuario");
    }

    private void mostrarAlerta(String mensajeRecibido) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mensaje");
        builder.setMessage(mensajeRecibido);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                llamarSiguienteActividad();
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();

    }

    private void llamarSiguienteActividad() {
        Intent intento = new Intent(this, MainActivity.class);
        startActivity(intento);
        finish();
    }


}