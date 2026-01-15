package com.lemas.lemascafeteria.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lemas.lemascafeteria.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtCorreo;
    EditText edtContrasena;
    Button btnRegistrarse;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtCorreo      = findViewById(R.id.edt_correo_register);
        edtContrasena  = findViewById(R.id.edt_contrasena_register);
        btnRegistrarse = findViewById(R.id.btn_ingresar_register);
        btnRegistrarse.setOnClickListener(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ingresar_register){
            String correoElectronico = edtCorreo.getText().toString();
            String contrasena = edtContrasena.getText().toString();
            if (correoElectronico.isEmpty()){
                alertaMensaje("Error", "No ha ingresado el correo electronico");
            } else if (contrasena.isEmpty()) {
                alertaMensaje("Error", "No ha ingresado la contraseña");
            } else {
                guardarUsuario(correoElectronico, contrasena);
            }
        }
    }

    private void guardarUsuario(String correoElectronico, String contrasena) {
        mAuth.createUserWithEmailAndPassword(correoElectronico, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Se ha registrado exitosamente al usuario", Toast.LENGTH_LONG).show();
                            RegisterActivity.this.finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void alertaMensaje(String titulo, String texto) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(titulo);
        alerta.setMessage(texto);
        alerta.show();
    }
}