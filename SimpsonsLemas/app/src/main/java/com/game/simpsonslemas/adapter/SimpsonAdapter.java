package com.game.simpsonslemas.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.game.simpsonslemas.DetailsActivity;
import com.game.simpsonslemas.MainActivity;
import com.game.simpsonslemas.R;
import com.game.simpsonslemas.models.Personajes;
import com.game.simpsonslemas.services.SimpsonApiService;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class SimpsonAdapter extends RecyclerView.Adapter<SimpsonAdapter.SimpsonViewHolder> {

    private List<Personajes> lstPersonajes;
    Context ctx;

    public SimpsonAdapter(Context context, List<Personajes> lstPersonajes) {
        this.lstPersonajes = lstPersonajes;
        this.ctx = context;
    }

    public void agregarPersonajes(List<Personajes> lstNuevosPersonajes) {
        int posicionInicial = lstPersonajes.size();
        lstPersonajes.addAll(lstNuevosPersonajes);
        notifyItemRangeInserted(posicionInicial, lstNuevosPersonajes.size());
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
        if (objPersonaje.getAge() == 0) {
            holder.tvEdadPersonaje.setVisibility(View.GONE);
        } else {
            holder.tvEdadPersonaje.setVisibility(View.VISIBLE);
            holder.tvEdadPersonaje.setText(objPersonaje.getAge()+"");
        }
        if(objPersonaje.getStatus().equalsIgnoreCase("Deceased")) {
            holder.tvVivoPersonaje.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rectangle_deceased_textview)
            );
            holder.tvVivoPersonaje.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.red)
            );
        } else {
            holder.tvVivoPersonaje.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rectangle_alive_textview)
            );
            holder.tvVivoPersonaje.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.green)
            );
        }
        holder.tvVivoPersonaje.setText(objPersonaje.getStatus());
        String[] frases = objPersonaje.getPhrases();
        String primeraFrase = (frases != null && frases.length > 0)
                ? frases[0]
                : "Sin frase";
        holder.tvFrasePersonaje.setText(primeraFrase.toString());
        holder.lytDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(ctx, DetailsActivity.class);
                intento.putExtra("personaje", objPersonaje);
                ctx.startActivity(intento);
            }
        });
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
        LinearLayout lytDetalle;

        public SimpsonViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPersonaje       = itemView.findViewById(R.id.img_personaje);
            tvNombrePersonaje  = itemView.findViewById(R.id.tv_nombre_personaje);
            tvTrabajoPersonaje = itemView.findViewById(R.id.tv_trabajo_personaje);
            tvEdadPersonaje    = itemView.findViewById(R.id.tv_edad_personaje);
            tvVivoPersonaje    = itemView.findViewById(R.id.tv_vivo_personaje);
            tvFrasePersonaje   = itemView.findViewById(R.id.tv_frase_personaje);
            lytDetalle         = itemView.findViewById(R.id.lyt_detalle);
        }
    }
}
