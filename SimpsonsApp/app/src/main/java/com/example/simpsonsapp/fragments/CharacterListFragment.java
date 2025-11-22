package com.example.simpsonsapp.fragments;

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

import com.example.simpsonsapp.MainActivity;
import com.example.simpsonsapp.R;
import com.example.simpsonsapp.adapter.CharacterAdapter;
import com.example.simpsonsapp.model.Character;
import com.example.simpsonsapp.model.CharacterListResponse;
import com.example.simpsonsapp.service.ApiClient;
import com.example.simpsonsapp.service.SimpsonsApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class CharacterListFragment extends Fragment {
    private RecyclerView recyclerView;
    private CharacterAdapter adapter;
    private ProgressBar progressBar;
    private List<Character> characterList;
    private int currentPage = 1;
    private boolean isLoading = false;
    private SimpsonsApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_list, container, false);
        
        recyclerView = view.findViewById(R.id.recycler_view_characters);
        progressBar = view.findViewById(R.id.progress_bar);
        
        characterList = new ArrayList<>();
        adapter = new CharacterAdapter(characterList, character -> {
            // Navegar al detalle del personaje
            CharacterDetailFragment detailFragment = new CharacterDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("character", character);
            detailFragment.setArguments(bundle);
            
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(detailFragment, true);
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        
        // Scroll listener para cargar más personajes
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    
                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadMoreCharacters();
                    }
                }
            }
        });
        
        apiService = ApiClient.getApiService();
        loadCharacters();
        
        return view;
    }
    
    private void loadCharacters() {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        
        Call<CharacterListResponse> call = apiService.getCharacters(currentPage);
        call.enqueue(new Callback<CharacterListResponse>() {
            @Override
            public void onResponse(Call<CharacterListResponse> call, Response<CharacterListResponse> response) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Character> newCharacters = response.body().getResults();
                    if (newCharacters != null && !newCharacters.isEmpty()) {
                        characterList.addAll(newCharacters);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No se encontraron personajes", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMsg = "Error al cargar personajes";
                    if (response.code() > 0) {
                        errorMsg += " (Código: " + response.code() + ")";
                    }
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<CharacterListResponse> call, Throwable t) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                String errorMsg = "Error de conexión";
                if (t.getMessage() != null) {
                    errorMsg += ": " + t.getMessage();
                }
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
    
    private void loadMoreCharacters() {
        if (isLoading) return;
        
        currentPage++;
        loadCharacters();
    }
}

