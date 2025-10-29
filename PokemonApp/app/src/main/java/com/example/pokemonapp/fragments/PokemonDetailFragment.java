package com.example.pokemonapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.pokemonapp.R;
import com.example.pokemonapp.model.Pokemon;
import com.example.pokemonapp.model.PokemonAbility;
import com.example.pokemonapp.model.PokemonStat;
import com.example.pokemonapp.model.PokemonType;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonDetailFragment extends Fragment {

    private Pokemon pokemon;
    private ImageView imageViewPokemon;
    private TextView textViewName;
    private TextView textViewId;
    private TextView textViewHeight;
    private TextView textViewWeight;
    private TextView textViewTypes;
    private TextView textViewAbilities;
    private TextView textViewStats;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_detail, container, false);
        
        initViews(view);
        loadPokemonData();
        
        return view;
    }

    private void initViews(View view) {
        imageViewPokemon = view.findViewById(R.id.image_view_pokemon);
        textViewName = view.findViewById(R.id.text_view_name);
        textViewId = view.findViewById(R.id.text_view_id);
        textViewHeight = view.findViewById(R.id.text_view_height);
        textViewWeight = view.findViewById(R.id.text_view_weight);
        textViewTypes = view.findViewById(R.id.text_view_types);
        textViewAbilities = view.findViewById(R.id.text_view_abilities);
        textViewStats = view.findViewById(R.id.text_view_stats);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void loadPokemonData() {
        if (getArguments() != null) {
            pokemon = (Pokemon) getArguments().getSerializable("pokemon");
            
            if (pokemon != null) {
                displayPokemonData();
            }
        }
    }

    private void displayPokemonData() {
        progressBar.setVisibility(View.GONE);
        
        // Cargar imagen
        if (pokemon.getSprites() != null && pokemon.getSprites().getFront_default() != null) {
            Picasso.get()
                    .load(pokemon.getSprites().getFront_default())
                    .into(imageViewPokemon);
        }
        
        // Mostrar información básica
        textViewName.setText(pokemon.getName().toUpperCase());
        textViewId.setText("ID: " + pokemon.getId());
        textViewHeight.setText("Altura: " + pokemon.getHeight() + " dm");
        textViewWeight.setText("Peso: " + pokemon.getWeight() + " hg");
        
        // Mostrar tipos
        if (pokemon.getTypes() != null && !pokemon.getTypes().isEmpty()) {
            StringBuilder typesText = new StringBuilder("Tipos: ");
            for (PokemonType type : pokemon.getTypes()) {
                typesText.append(type.getType().getName()).append(" ");
            }
            textViewTypes.setText(typesText.toString());
        }
        
        // Mostrar habilidades
        if (pokemon.getAbilities() != null && !pokemon.getAbilities().isEmpty()) {
            StringBuilder abilitiesText = new StringBuilder("Habilidades: ");
            for (PokemonAbility ability : pokemon.getAbilities()) {
                abilitiesText.append(ability.getAbility().getName()).append(" ");
            }
            textViewAbilities.setText(abilitiesText.toString());
        }
        
        // Mostrar estadísticas
        if (pokemon.getStats() != null && !pokemon.getStats().isEmpty()) {
            StringBuilder statsText = new StringBuilder("Estadísticas:\n");
            for (PokemonStat stat : pokemon.getStats()) {
                statsText.append(stat.getStat().getName())
                        .append(": ")
                        .append(stat.getBase_stat())
                        .append("\n");
            }
            textViewStats.setText(statsText.toString());
        }
    }
}
