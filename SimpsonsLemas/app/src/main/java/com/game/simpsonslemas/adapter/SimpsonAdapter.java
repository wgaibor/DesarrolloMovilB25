package com.game.simpsonslemas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.simpsonslemas.R;
import com.game.simpsonslemas.models.Personajes;
import com.game.simpsonslemas.services.SimpsonApiService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimpsonAdapter extends RecyclerView.Adapter<SimpsonAdapter.SimpsonViewHolder> {

    private List<Personajes> lstPersonajes;

    public SimpsonAdapter(List<Personajes> lstPersonajes) {
        this.lstPersonajes = lstPersonajes;
    }

    @NonNull
    @Override
    public SimpsonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_simpsons_layout, parent, false);
        SimpsonViewHolder viewHolder = new SimpsonViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpsonViewHolder holder, int position) {
        Personajes objPersonaje = lstPersonajes.get(position);
        String urlImagen = "https://cdn.thesimpsonsapi.com/200"+objPersonaje.getPortrait_path();
        Picasso.get()
                .load(urlImagen)
                .into(holder.imgPersonaje);
        holder.tvNombrePersonaje.setText(objPersonaje.getName());
        holder.tvTrabajoPersonaje.setText(objPersonaje.getOccupation());
        String edad = String.valueOf(objPersonaje.getAge()) != null ? String.valueOf(objPersonaje.getAge()) : "NO TIENE EDAD";
        holder.tvEdadPersonaje.setText( edad);
        holder.tvVivoPersonaje.setText(objPersonaje.getStatus());
        String[] frases = objPersonaje.getPhrases();
        String primeraFrase = (frases != null && frases.length > 0)
                ? frases[0]
                : "Sin frase";
        holder.tvFrasePersonaje.setText(primeraFrase);

    }

    @Override
    public int getItemCount() {
        return lstPersonajes.size();
    }

    static class SimpsonViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPersonaje;
        TextView tvNombrePersonaje;
        TextView tvTrabajoPersonaje;
        TextView tvEdadPersonaje;
        TextView tvVivoPersonaje;
        TextView tvFrasePersonaje;

        public SimpsonViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPersonaje = itemView.findViewById(R.id.img_personaje);
            tvNombrePersonaje = itemView.findViewById(R.id.tv_nombre_personaje);
            tvTrabajoPersonaje = itemView.findViewById(R.id.tv_trabajo_personaje);
            tvEdadPersonaje = itemView.findViewById(R.id.tv_edad_personaje);
            tvVivoPersonaje = itemView.findViewById(R.id.tv_vivo_personaje);
            tvFrasePersonaje = itemView.findViewById(R.id.tv_frase_personaje);
        }
    }
}
