package ec.game.pokemon;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import ec.game.pokemon.model.Pokemon;
import ec.game.pokemon.model.PokemonStat;

public class DetailsActivity extends AppCompatActivity {

    ImageView imgPokemon;
    TextView tvNombrePokemon;
    TextView tvIdPokemon;
    TextView tvAlturaPokemon;
    TextView tvPesoPokemon;
    TextView tvTipoPokemon;
    TextView tvHabilidadesPokemon;
    TextView tvEstadisticas;

    Pokemon valorRecibido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imgPokemon = findViewById(R.id.img_detail_pokemon);
        tvNombrePokemon = findViewById(R.id.tv_name_detail_pokemon);
        tvIdPokemon = findViewById(R.id.tv_id_detail_pokemon);
        tvAlturaPokemon = findViewById(R.id.tv_altura);
        tvPesoPokemon = findViewById(R.id.tv_peso);
        tvTipoPokemon = findViewById(R.id.tv_tipo_pokemon);
        tvHabilidadesPokemon = findViewById(R.id.tv_habilidades_pokemon);
        tvEstadisticas = findViewById(R.id.tv_estadistica);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            valorRecibido = (Pokemon) extras.getSerializable("pokemon");
        }
        if (valorRecibido.getSprites() != null && valorRecibido.getSprites().getFront_default() != null) {
            Picasso.get()
                    .load(valorRecibido.getSprites().getFront_default())
                    .into(imgPokemon);
        }

        tvNombrePokemon.setText(valorRecibido.getName());
        tvIdPokemon.setText("ID: "+valorRecibido.getId()+"");
        tvPesoPokemon.setText("PESO: "+valorRecibido.getWeight()+" hg");
        tvAlturaPokemon.setText("ALTURA:  "+valorRecibido.getHeight()+ " dm");

        if (valorRecibido.getTypes() != null && !valorRecibido.getTypes().isEmpty()) {
            StringBuilder concatenacionTexto = new StringBuilder();
            for (int inc = 0; inc < valorRecibido.getTypes().size(); inc++){
                concatenacionTexto.append(valorRecibido.getTypes().get(inc).getType().getName());
                if(inc < (valorRecibido.getTypes().size() - 1) ) {
                    concatenacionTexto.append(" , ");
                }
            }
            tvTipoPokemon.setText("TIPO:  "+concatenacionTexto);
        }

        if (valorRecibido.getAbilities() != null) {
            StringBuilder concatenacionTexto = new StringBuilder();
            for (int inc = 0; inc < valorRecibido.getAbilities().size(); inc++){
                concatenacionTexto.append(valorRecibido.getAbilities().get(inc).getAbility().getName());
                if(inc < (valorRecibido.getTypes().size() - 1) ) {
                    concatenacionTexto.append(" , ");
                }
            }
            tvHabilidadesPokemon.setText("HABILIDADES:  "+concatenacionTexto);
        }

        if (valorRecibido.getStats() != null){
            StringBuilder estadisticasPokemon = new StringBuilder("Estadistica: \n");
            for (PokemonStat estadistica: valorRecibido.getStats() ) {
                estadisticasPokemon.append(estadistica.getStat().getName())
                        .append(": ")
                        .append(estadistica.getBase_stat())
                        .append("\n");
            }
            tvEstadisticas.setText(estadisticasPokemon);
        }
    }
}