package com.lemas.cafeteria.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lemas.cafeteria.R;
import com.lemas.cafeteria.model.Articulo;
import com.lemas.cafeteria.util.Constants;

import java.util.List;

/**
 * Adapter para mostrar la lista de artículos en un RecyclerView.
 * Maneja la visualización de artículos con su imagen, nombre y precio.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder> {

    /** Lista de artículos a mostrar */
    private List<Articulo> articulos;
    
    /** Listener para manejar clicks en los artículos */
    private final OnItemClickListener listener;

    /**
     * Interfaz para manejar clicks en los items del RecyclerView.
     */
    public interface OnItemClickListener {
        /**
         * Se ejecuta cuando se hace click en un artículo.
         * 
         * @param articulo Artículo seleccionado
         */
        void onItemClick(Articulo articulo);
    }

    /**
     * Constructor del adapter.
     * 
     * @param articulos Lista de artículos a mostrar
     * @param listener Listener para manejar clicks en los artículos
     */
    public ArticuloAdapter(List<Articulo> articulos, OnItemClickListener listener) {
        this.articulos = articulos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_articulo, parent, false);
        return new ArticuloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int position) {
        if (position >= 0 && position < articulos.size()) {
            Articulo articulo = articulos.get(position);
            holder.bind(articulo);
        }
    }

    @Override
    public int getItemCount() {
        return articulos != null ? articulos.size() : 0;
    }

    /**
     * Actualiza la lista de artículos y notifica al adapter.
     * 
     * @param newList Nueva lista de artículos
     */
    public void updateList(List<Articulo> newList) {
        this.articulos = newList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para los items del RecyclerView.
     * Mantiene referencias a las vistas para mejorar el rendimiento.
     */
    class ArticuloViewHolder extends RecyclerView.ViewHolder {
        
        /** ImageView para mostrar la imagen del artículo */
        private final ImageView imagenArticulo;
        
        /** TextView para mostrar el nombre del artículo */
        private final TextView nombreArticulo;
        
        /** TextView para mostrar el precio del artículo */
        private final TextView precioArticulo;

        /**
         * Constructor del ViewHolder.
         * 
         * @param itemView Vista del item
         */
        public ArticuloViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenArticulo = itemView.findViewById(R.id.imagen_articulo);
            nombreArticulo = itemView.findViewById(R.id.nombre_articulo);
            precioArticulo = itemView.findViewById(R.id.precio_articulo);

            // Configurar click listener en el item completo
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(articulos.get(position));
                }
            });
        }

        /**
         * Vincula los datos del artículo a las vistas.
         * 
         * @param articulo Artículo a mostrar
         */
        public void bind(Articulo articulo) {
            if (articulo == null) {
                return;
            }

            nombreArticulo.setText(articulo.getNombre());
            precioArticulo.setText(String.format(Constants.FORMATO_PRECIO, articulo.getPrecioUnitario()));

            // Cargar imagen con Glide
            cargarImagen(articulo.getImagenUrl());
        }

        /**
         * Carga la imagen del artículo usando Glide.
         * 
         * @param imagenUrl URL de la imagen a cargar
         */
        private void cargarImagen(String imagenUrl) {
            if (imagenUrl != null && !imagenUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(imagenUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(imagenArticulo);
            } else {
                imagenArticulo.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}
