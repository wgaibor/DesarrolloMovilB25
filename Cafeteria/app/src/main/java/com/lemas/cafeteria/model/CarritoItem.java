package com.lemas.cafeteria.model;

/**
 * Modelo que representa un item en el carrito de compras.
 * Contiene un artículo y su cantidad seleccionada.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class CarritoItem {
    
    /** Artículo agregado al carrito */
    private Articulo articulo;
    
    /** Cantidad del artículo en el carrito */
    private int cantidad;

    /**
     * Constructor para crear un item del carrito.
     * 
     * @param articulo Artículo a agregar al carrito
     * @param cantidad Cantidad del artículo
     */
    public CarritoItem(Articulo articulo, int cantidad) {
        this.articulo = articulo;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el artículo del carrito.
     * 
     * @return Artículo del carrito
     */
    public Articulo getArticulo() {
        return articulo;
    }

    /**
     * Establece el artículo del carrito.
     * 
     * @param articulo Artículo del carrito
     */
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    /**
     * Obtiene la cantidad del artículo en el carrito.
     * 
     * @return Cantidad del artículo
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del artículo en el carrito.
     * 
     * @param cantidad Cantidad del artículo (debe ser mayor a 0)
     */
    public void setCantidad(int cantidad) {
        if (cantidad > 0) {
            this.cantidad = cantidad;
        }
    }

    /**
     * Calcula el subtotal del item (precio unitario * cantidad).
     * 
     * @return Subtotal del item
     */
    public double getSubtotal() {
        if (articulo == null) {
            return 0.0;
        }
        return articulo.getPrecioUnitario() * cantidad;
    }
}
