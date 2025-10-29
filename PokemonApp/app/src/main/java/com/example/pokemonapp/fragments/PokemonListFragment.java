package com.example.pokemonapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapp.MainActivity;
import com.example.pokemonapp.R;
import com.example.pokemonapp.adapter.PokemonAdapter;
import com.example.pokemonapp.model.Pokemon;
import com.example.pokemonapp.model.PokemonListResponse;
import com.example.pokemonapp.service.PokemonApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class PokemonListFragment extends Fragment implements PokemonAdapter.OnPokemonClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PokemonAdapter adapter;
    private List<Pokemon> pokemonList;
    private PokemonApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_list, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadPokemonList();
        
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_pokemon);
        progressBar = view.findViewById(R.id.progress_bar);
        pokemonList = new ArrayList<>();
        apiService = PokemonApiService.getInstance();
    }

    private void setupRecyclerView() {
        adapter = new PokemonAdapter(pokemonList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadPokemonList() {
        progressBar.setVisibility(View.VISIBLE);
        
        apiService.getApi().getPokemonList(20, 0).enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    loadPokemonDetails(response.body().getResults());
                } else {
                    Toast.makeText(getContext(), "Error al cargar la lista de Pokémon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPokemonDetails(List<PokemonListResponse.PokemonListItem> pokemonItems) {
        for (PokemonListResponse.PokemonListItem item : pokemonItems) {
            String pokemonName = item.getName();
            apiService.getApi().getPokemonByName(pokemonName).enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pokemonList.add(response.body());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                    // Manejar error individual
                }
            });
        }
    }

    @Override
    public void onPokemonClick(Pokemon pokemon) {
        // Navegar al fragment de detalles
        PokemonDetailFragment detailFragment = new PokemonDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("pokemon", pokemon);
        detailFragment.setArguments(bundle);
        
        if (getActivity() != null) {
            ((MainActivity) getActivity()).loadFragment(detailFragment);
        }
    }
}
