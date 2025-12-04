package com.game.simpsonslemas;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.game.simpsonslemas.models.Personajes;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    ImageView imgPersonaje;
    TextView tvNombrePersonaje;
    TextView tvOcupacionPersonaje;
    TextView tvEdadPersonaje;
    TextView tvEstadoPersonaje;
    TextView tvFrasePersonaje;

    Personajes personaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imgPersonaje         = findViewById(R.id.img_detalle_personaje);
        tvNombrePersonaje    = findViewById(R.id.tv_detalle_nombre);
        tvOcupacionPersonaje = findViewById(R.id.tv_detalle_ocupacion);
        tvEdadPersonaje      = findViewById(R.id.tv_detalle_edad);
        tvEstadoPersonaje    = findViewById(R.id.tv_detalle_estado);
        tvFrasePersonaje     = findViewById(R.id.tv_detalle_frase);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            personaje = (Personajes) extras.getSerializable("personaje");
        }

        String urlImagen = "https://cdn.thesimpsonsapi.com/500"+personaje.getPortrait_path();
        Picasso.get()
                .load(urlImagen)
                .into(imgPersonaje);
        tvNombrePersonaje.setText(personaje.getName());
        tvOcupacionPersonaje.setText(personaje.getOccupation());
        if (personaje.getAge() != 0) {
            tvEdadPersonaje.setText(personaje.getAge()+"");
        } else {
            tvEdadPersonaje.setVisibility(View.GONE);
        }
        if (personaje.getStatus().equalsIgnoreCase("Deceased")) {
            tvEstadoPersonaje.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_deceased_textview));
        }
        tvEstadoPersonaje.setText(personaje.getStatus());
        StringBuilder textoConcatenado = new StringBuilder();
        textoConcatenado.append("\t\t Frase del personaje: \n \n");
        for (String textoFrase: personaje.getPhrases()) {
            textoConcatenado.append(" - "+textoFrase + "\n");
        }
        tvFrasePersonaje.setText(textoConcatenado);
    }
}