package com.lemas.cafeteria.util;

import com.lemas.cafeteria.model.Articulo;
import com.lemas.cafeteria.model.CarritoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor singleton para el carrito de compras.
 * Maneja todas las operaciones relacionadas con el carrito:
 * agregar, eliminar, actualizar cantidades y calcular totales.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class CarritoManager {
    
    /** Instancia única del gestor (Singleton) */
    private static CarritoManager instance;
    
    /** Lista de items en el carrito */
    private final List<CarritoItem> items;

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private CarritoManager() {
        items = new ArrayList<>();
    }

    /**
     * Obtiene la instancia única del CarritoManager.
     * 
     * @return Instancia única del CarritoManager
     */
    public static CarritoManager getInstance() {
        if (instance == null) {
            synchronized (CarritoManager.class) {
                if (instance == null) {
                    instance = new CarritoManager();
                }
            }
        }
        return instance;
    }

    /**
     * Agrega un artículo al carrito. Si el artículo ya existe,
     * incrementa su cantidad en 1.
     * 
     * @param articulo Artículo a agregar al carrito
     */
    public void agregarArticulo(Articulo articulo) {
        if (articulo == null || articulo.getId() == null) {
            return;
        }
        
        // Buscar si el artículo ya existe en el carrito
        for (CarritoItem item : items) {
            if (item.getArticulo().getId().equals(articulo.getId())) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
        }
        
        // Si no existe, agregarlo con cantidad 1
        items.add(new CarritoItem(articulo, Constants.CANTIDAD_MINIMA));
    }

    /**
     * Elimina un artículo del carrito por su ID.
     * 
     * @param articuloId ID del artículo a eliminar
     */
    public void eliminarArticulo(String articuloId) {
        if (articuloId == null || articuloId.isEmpty()) {
            return;
        }
        items.removeIf(item -> item.getArticulo().getId().equals(articuloId));
    }

    /**
     * Actualiza la cantidad de un artículo en el carrito.
     * Si la cantidad es menor o igual a 0, elimina el artículo.
     * 
     * @param articuloId ID del artículo a actualizar
     * @param cantidad Nueva cantidad del artículo
     */
    public void actualizarCantidad(String articuloId, int cantidad) {
        if (articuloId == null || articuloId.isEmpty()) {
            return;
        }
        
        for (CarritoItem item : items) {
            if (item.getArticulo().getId().equals(articuloId)) {
                if (cantidad <= 0) {
                    eliminarArticulo(articuloId);
                } else {
                    item.setCantidad(cantidad);
                }
                return;
            }
        }
    }

    /**
     * Obtiene la lista de items en el carrito.
     * 
     * @return Lista inmutable de items del carrito
     */
    public List<CarritoItem> getItems() {
        return new ArrayList<>(items); // Retornar copia para evitar modificaciones externas
    }

    /**
     * Calcula el total del carrito sumando todos los subtotales.
     * 
     * @return Total del carrito en formato double
     */
    public double getTotal() {
        double total = 0.0;
        for (CarritoItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Calcula la cantidad total de artículos en el carrito.
     * 
     * @return Cantidad total de artículos
     */
    public int getCantidadTotal() {
        int cantidad = 0;
        for (CarritoItem item : items) {
            cantidad += item.getCantidad();
        }
        return cantidad;
    }

    /**
     * Limpia todos los items del carrito.
     */
    public void limpiarCarrito() {
        items.clear();
    }

    /**
     * Verifica si el carrito está vacío.
     * 
     * @return true si el carrito está vacío, false en caso contrario
     */
    public boolean estaVacio() {
        return items.isEmpty();
    }
}
