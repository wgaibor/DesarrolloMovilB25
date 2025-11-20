package ec.game.pokemon;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ec.game.pokemon.adapter.PokemonAdapter;
import ec.game.pokemon.model.Pokemon;
import ec.game.pokemon.model.PokemonListItems;
import ec.game.pokemon.model.PokemonListResponse;
import ec.game.pokemon.service.PokemonApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private PokemonApiService apiService;

    private List<Pokemon> lstPokemon;

    RecyclerView rvPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvPokemon = findViewById(R.id.rv_Pokemon);
        loadData();
    }

    private void loadData() {
        apiService = PokemonApiService.getInstance();

        apiService.getApi().getPokemonList(7, 0).enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("-------", response.body().getResults().get(0).getName());
                    loadPokemonDetails(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR DE CONEXIÓN "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPokemonDetails(List<PokemonListItems> results) {
        lstPokemon = new ArrayList<>();
        for (PokemonListItems itemPokemon: results) {
            String namePokemon = itemPokemon.getName();
            apiService.getApi().getPokemonByName(namePokemon).enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        lstPokemon.add(response.body());
                        showPokemon(lstPokemon);
                    }
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {

                }
            });
        }
    }

    private void showPokemon(List<Pokemon> lstPokemon) {
        PokemonAdapter pokemonAdapter = new PokemonAdapter(lstPokemon, this);
        rvPokemon.setHasFixedSize(true);
        rvPokemon.setLayoutManager(new LinearLayoutManager(this));
        rvPokemon.setAdapter(pokemonAdapter);

    }


}