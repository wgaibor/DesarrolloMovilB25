package com.lemas.cafeteria.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lemas.cafeteria.R;
import com.lemas.cafeteria.model.CarritoItem;
import com.lemas.cafeteria.util.CarritoManager;
import com.lemas.cafeteria.util.Constants;

import java.util.List;

/**
 * Adapter para mostrar los items del carrito de compras en un RecyclerView.
 * Permite modificar cantidades y eliminar items del carrito.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    /** Lista de items en el carrito */
    private List<CarritoItem> items;
    
    /** Callback para actualizar el total cuando cambia la lista */
    private final Runnable onTotalChanged;
    
    /** Callback para actualizar la lista cuando cambia un item */
    private final Runnable onListChanged;

    /**
     * Constructor del adapter.
     * 
     * @param items Lista de items del carrito
     * @param onTotalChanged Callback para actualizar el total
     * @param onListChanged Callback para actualizar la lista
     */
    public CarritoAdapter(List<CarritoItem> items, Runnable onTotalChanged, Runnable onListChanged) {
        this.items = items;
        this.onTotalChanged = onTotalChanged;
        this.onListChanged = onListChanged;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        if (position >= 0 && position < items.size()) {
            CarritoItem item = items.get(position);
            holder.bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * Actualiza la lista de items y notifica al adapter.
     * 
     * @param newList Nueva lista de items
     */
    public void updateList(List<CarritoItem> newList) {
        this.items = newList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para los items del carrito.
     */
    class CarritoViewHolder extends RecyclerView.ViewHolder {
        
        private final ImageView imagenArticulo;
        private final TextView nombreArticulo;
        private final TextView precioUnitario;
        private final TextView cantidad;
        private final TextView subtotal;
        private final ImageButton btnMenos;
        private final ImageButton btnMas;
        private final ImageButton btnEliminar;

        /**
         * Constructor del ViewHolder.
         * 
         * @param itemView Vista del item
         */
        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenArticulo = itemView.findViewById(R.id.imagen_articulo);
            nombreArticulo = itemView.findViewById(R.id.nombre_articulo);
            precioUnitario = itemView.findViewById(R.id.precio_unitario);
            cantidad = itemView.findViewById(R.id.cantidad);
            subtotal = itemView.findViewById(R.id.subtotal);
            btnMenos = itemView.findViewById(R.id.btn_menos);
            btnMas = itemView.findViewById(R.id.btn_mas);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
        }

        /**
         * Vincula los datos del item del carrito a las vistas.
         * 
         * @param carritoItem Item del carrito a mostrar
         */
        public void bind(CarritoItem carritoItem) {
            if (carritoItem == null || carritoItem.getArticulo() == null) {
                return;
            }

            // Mostrar información del artículo
            nombreArticulo.setText(carritoItem.getArticulo().getNombre());
            precioUnitario.setText(String.format(Constants.FORMATO_PRECIO, 
                    carritoItem.getArticulo().getPrecioUnitario()));
            cantidad.setText(String.valueOf(carritoItem.getCantidad()));
            subtotal.setText(String.format(Constants.FORMATO_PRECIO, carritoItem.getSubtotal()));

            // Cargar imagen
            cargarImagen(carritoItem.getArticulo().getImagenUrl());

            // Configurar listeners de los botones
            configurarListeners(carritoItem);
        }

        /**
         * Carga la imagen del artículo usando Glide.
         * 
         * @param imagenUrl URL de la imagen
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

        /**
         * Configura los listeners de los botones de acción.
         * 
         * @param carritoItem Item del carrito
         */
        private void configurarListeners(CarritoItem carritoItem) {
            String articuloId = carritoItem.getArticulo().getId();
            
            // Botón para disminuir cantidad
            btnMenos.setOnClickListener(v -> {
                int nuevaCantidad = carritoItem.getCantidad() - 1;
                CarritoManager.getInstance().actualizarCantidad(articuloId, nuevaCantidad);
                notificarCambios();
            });

            // Botón para aumentar cantidad
            btnMas.setOnClickListener(v -> {
                int nuevaCantidad = carritoItem.getCantidad() + 1;
                CarritoManager.getInstance().actualizarCantidad(articuloId, nuevaCantidad);
                notificarCambios();
            });

            // Botón para eliminar del carrito
            btnEliminar.setOnClickListener(v -> {
                CarritoManager.getInstance().eliminarArticulo(articuloId);
                notificarCambios();
            });
        }

        /**
         * Notifica los cambios al adapter y actualiza el total.
         */
        private void notificarCambios() {
            if (onListChanged != null) {
                onListChanged.run();
            }
            if (onTotalChanged != null) {
                onTotalChanged.run();
            }
        }
    }
}
