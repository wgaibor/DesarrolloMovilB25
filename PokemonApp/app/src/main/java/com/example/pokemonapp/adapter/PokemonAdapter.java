package com.example.pokemonapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pokemonapp.R;
import com.example.pokemonapp.model.Pokemon;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemonList;
    private OnPokemonClickListener listener;

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    public PokemonAdapter(List<Pokemon> pokemonList, OnPokemonClickListener listener) {
        this.pokemonList = pokemonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.bind(pokemon, listener);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPokemon;
        private TextView textViewName;
        private TextView textViewId;
        private TextView textViewTypes;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPokemon = itemView.findViewById(R.id.image_view_pokemon);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewId = itemView.findViewById(R.id.text_view_id);
            textViewTypes = itemView.findViewById(R.id.text_view_types);
        }

        public void bind(Pokemon pokemon, OnPokemonClickListener listener) {
            // Cargar imagen
            if (pokemon.getSprites() != null && pokemon.getSprites().getFront_default() != null) {
                Picasso.get()
                        .load(pokemon.getSprites().getFront_default())
                        .placeholder(R.drawable.ic_pokemon_placeholder)
                        .error(R.drawable.ic_pokemon_placeholder)
                        .into(imageViewPokemon);
            }

            // Mostrar información
            textViewName.setText(pokemon.getName().toUpperCase());
            textViewId.setText("ID: " + pokemon.getId());

            // Mostrar tipos
            if (pokemon.getTypes() != null && !pokemon.getTypes().isEmpty()) {
                StringBuilder typesText = new StringBuilder();
                for (int i = 0; i < pokemon.getTypes().size(); i++) {
                    typesText.append(pokemon.getTypes().get(i).getType().getName());
                    if (i < pokemon.getTypes().size() - 1) {
                        typesText.append(", ");
                    }
                }
                textViewTypes.setText(typesText.toString());
            }

            // Configurar click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPokemonClick(pokemon);
                }
            });
        }
    }
}
