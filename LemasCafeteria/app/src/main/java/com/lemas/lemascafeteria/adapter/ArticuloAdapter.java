package com.lemas.lemascafeteria.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemas.lemascafeteria.R;

/**
 * Adapter para mostrar la lista de artículos en un RecyclerView.
 * Maneja la visualización de artículos con su imagen, nombre y precio.
 *
 * @author Cafeteria App
 * @version 1.0
 */
public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder> {

    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ArticuloViewHolder extends RecyclerView.ViewHolder {
        /** ImageView para mostrar la imagen del artículo */
        private final ImageView imagenArticulo;
        /** TextView para mostrar el nombre del artículo */
        private final TextView nombreArticulo;
        /** TextView para mostrar el precio del artículo */
        private final TextView precioArticulo;
        public ArticuloViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenArticulo = itemView.findViewById(R.id.imagen_articulo);
            nombreArticulo = itemView.findViewById(R.id.nombre_articulo);
            precioArticulo = itemView.findViewById(R.id.precio_articulo);
        }
    }
}
