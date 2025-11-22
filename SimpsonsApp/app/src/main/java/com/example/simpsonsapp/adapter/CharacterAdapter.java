package com.example.simpsonsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.simpsonsapp.R;
import com.example.simpsonsapp.model.Character;
import com.bumptech.glide.Glide;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private List<Character> characterList;
    private OnCharacterClickListener listener;

    public interface OnCharacterClickListener {
        void onCharacterClick(Character character);
    }

    public CharacterAdapter(List<Character> characterList, OnCharacterClickListener listener) {
        this.characterList = characterList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character character = characterList.get(position);
        holder.bind(character);
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    class CharacterViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imageView;
        private TextView nameTextView;
        private TextView occupationTextView;
        private TextView statusTextView;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_character);
            imageView = itemView.findViewById(R.id.image_character);
            nameTextView = itemView.findViewById(R.id.text_name);
            occupationTextView = itemView.findViewById(R.id.text_occupation);
            statusTextView = itemView.findViewById(R.id.text_status);
        }

        public void bind(Character character) {
            nameTextView.setText(character.getName());
            
            if (character.getOccupation() != null && !character.getOccupation().isEmpty()) {
                occupationTextView.setText(character.getOccupation());
            } else {
                occupationTextView.setText("Ocupación desconocida");
            }
            
            if (character.getStatus() != null && !character.getStatus().isEmpty()) {
                statusTextView.setText(character.getStatus());
            } else {
                statusTextView.setText("Estado desconocido");
            }
            
            // Cargar imagen
            String imageUrl = character.getFullImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(imageView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_character_placeholder)
                        .error(R.drawable.ic_character_placeholder)
                        .override(150, 150)
                        .centerCrop()
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.ic_character_placeholder);
            }
            
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCharacterClick(character);
                }
            });
        }
    }
}

