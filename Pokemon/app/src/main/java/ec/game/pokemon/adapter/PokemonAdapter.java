package ec.game.pokemon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ec.game.pokemon.DetailsActivity;
import ec.game.pokemon.R;
import ec.game.pokemon.model.Pokemon;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    List<Pokemon> lstPokemon;
    Context context;

    public PokemonAdapter(List<Pokemon> lstPokemon, Context ctx) {
        this.lstPokemon = lstPokemon;
        this.context = ctx;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.card_items, parent, false);
        PokemonViewHolder viewHolder = new PokemonViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon objPokemon = lstPokemon.get(position);
        holder.tvNamePokemon.setText(objPokemon.getName());
        holder.tvIdPokemon.setText("ID: "+objPokemon.getId());

        if (objPokemon.getTypes() != null && !objPokemon.getTypes().isEmpty()) {
            StringBuilder concatenacionTexto = new StringBuilder();
            for (int inc = 0; inc < objPokemon.getTypes().size(); inc++){
                concatenacionTexto.append(objPokemon.getTypes().get(inc).getType().getName());
                if(inc < (objPokemon.getTypes().size() - 1) ) {
                    concatenacionTexto.append(" , ");
                }
            }
            holder.tvTypePokemon.setText(concatenacionTexto);
        }

        if (objPokemon.getSprites() != null && objPokemon.getSprites().getFront_default() != null) {
            Picasso.get()
                    .load(objPokemon.getSprites().getFront_default())
                    .into(holder.imgPokemon);
        }
        holder.cvItemPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(context, DetailsActivity.class);
                intento.putExtra("pokemon", objPokemon);
                context.startActivity(intento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstPokemon.size();
    }

    static class PokemonViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPokemon;
        private TextView tvNamePokemon;
        private TextView tvIdPokemon;
        private TextView tvTypePokemon;
        private CardView cvItemPokemon;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgPokemon = itemView.findViewById(R.id.img_Pokemon);
            this.tvNamePokemon = itemView.findViewById(R.id.tv_name_pokemon);
            this.tvIdPokemon = itemView.findViewById(R.id.tv_id_pokemon);
            this.tvTypePokemon = itemView.findViewById(R.id.tv_type_pokemon);
            this.cvItemPokemon = itemView.findViewById(R.id.cv_item_pokemon);
        }
    }
}
