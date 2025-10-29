package ec.edu.holamundo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    EditText nombre;
    Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = findViewById(R.id.edtNombre);
        enviar = findViewById(R.id.btnEnviar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Te llamas "+nombre.getText(), Toast.LENGTH_SHORT).show();
                Snackbar.make(MainActivity.this.getCurrentFocus(), "Te llamas "+nombre.getText(), Snackbar.LENGTH_SHORT).show();
                alertar("Te llamas "+nombre.getText());
            }
        });
    }

    private void alertar(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getClass().getSimpleName());
        builder.setMessage(mensaje);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(MainActivity.this.getCurrentFocus(), "Todo bien!", Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}