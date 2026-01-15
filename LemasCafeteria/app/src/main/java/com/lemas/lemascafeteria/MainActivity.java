package com.lemas.lemascafeteria;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.lemas.lemascafeteria.activity.MenuActivity;
import com.lemas.lemascafeteria.activity.RegisterActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnIngresar;
    Button btnRegistrar;

    EditText edtCorreo;
    EditText edtContrasena;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIngresar   = findViewById(R.id.btn_ingresar);
        btnRegistrar  = findViewById(R.id.btn_registrar);
        edtCorreo     = findViewById(R.id.edt_correo);
        edtContrasena = findViewById(R.id.edt_contrasena);
        btnRegistrar.setOnClickListener(this);
        btnIngresar.setOnClickListener(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intento = new Intent(this, MenuActivity.class);
            startActivity(intento);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_registrar){
            Intent intento = new Intent(this, RegisterActivity.class);
            startActivity(intento);
        } else if (v.getId() == R.id.btn_ingresar) {
            String correo = edtCorreo.getText().toString();
            String contrasena = edtContrasena.getText().toString();
            if (correo.isEmpty()){
                alertaMensaje("Error", "No ha ingresado el correo electronico");
            } else if (contrasena.isEmpty()) {
                alertaMensaje("Error", "No ha ingresado la contraseña");
            } else {
                validarUsuario(correo, contrasena);
            }
        }
    }

    private void validarUsuario(String correo, String contrasena) {
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Te has logueado exitosamente", Toast.LENGTH_LONG).show();
                            Intent intento = new Intent(MainActivity.this, MenuActivity.class);
                            MainActivity.this.startActivity(intento);
                            MainActivity.this.finish();
                        } else {
                            alertaMensaje("Login Fallido", "Las credenciales son incorrectas");
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