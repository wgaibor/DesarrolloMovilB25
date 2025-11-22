package com.example.simpsonsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.simpsonsapp.R;
import com.example.simpsonsapp.model.Character;
import com.bumptech.glide.Glide;
import java.util.List;

public class CharacterDetailFragment extends Fragment {
    private Character character;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_detail, container, false);
        
        if (getArguments() != null) {
            character = (Character) getArguments().getSerializable("character");
        }
        
        // Configurar toolbar
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        
        if (character != null) {
            displayCharacterDetails(view);
        }
        
        return view;
    }
    
    private void displayCharacterDetails(View view) {
        ImageView imageView = view.findViewById(R.id.image_character);
        TextView nameTextView = view.findViewById(R.id.text_name);
        TextView ageTextView = view.findViewById(R.id.text_age);
        TextView genderTextView = view.findViewById(R.id.text_gender);
        TextView occupationTextView = view.findViewById(R.id.text_occupation);
        TextView birthdateTextView = view.findViewById(R.id.text_birthdate);
        TextView statusTextView = view.findViewById(R.id.text_status);
        TextView phrasesTextView = view.findViewById(R.id.text_phrases);
        
        // Cargar imagen
        String imageUrl = character.getFullImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_character_placeholder)
                    .error(R.drawable.ic_character_placeholder)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_character_placeholder);
        }
        
        // Mostrar información
        nameTextView.setText(character.getName());
        
        if (character.getAge() != null) {
            ageTextView.setText("Edad: " + character.getAge() + " años");
        } else {
            ageTextView.setText("Edad: Desconocida");
        }
        
        if (character.getGender() != null && !character.getGender().isEmpty()) {
            genderTextView.setText("Género: " + character.getGender());
        } else {
            genderTextView.setText("Género: Desconocido");
        }
        
        if (character.getOccupation() != null && !character.getOccupation().isEmpty()) {
            occupationTextView.setText("Ocupación: " + character.getOccupation());
        } else {
            occupationTextView.setText("Ocupación: Desconocida");
        }
        
        if (character.getBirthdate() != null && !character.getBirthdate().isEmpty()) {
            birthdateTextView.setText("Fecha de nacimiento: " + character.getBirthdate());
        } else {
            birthdateTextView.setText("Fecha de nacimiento: Desconocida");
        }
        
        if (character.getStatus() != null && !character.getStatus().isEmpty()) {
            statusTextView.setText("Estado: " + character.getStatus());
        } else {
            statusTextView.setText("Estado: Desconocido");
        }
        
        // Mostrar frases
        List<String> phrases = character.getPhrases();
        if (phrases != null && !phrases.isEmpty()) {
            StringBuilder phrasesText = new StringBuilder("Frases famosas:\n");
            for (int i = 0; i < Math.min(5, phrases.size()); i++) {
                phrasesText.append("• ").append(phrases.get(i)).append("\n");
            }
            phrasesTextView.setText(phrasesText.toString());
        } else {
            phrasesTextView.setText("No hay frases disponibles");
        }
    }
}

