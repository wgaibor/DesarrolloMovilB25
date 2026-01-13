package com.lemas.cafeteria.model;

/**
 * Modelo de datos que representa un artículo (desayuno o bebida) en la aplicación.
 * Esta clase se utiliza para almacenar y recuperar datos de Firestore.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class Articulo {
    
    /** ID único del artículo en Firestore */
    private String id;
    
    /** Nombre del artículo */
    private String nombre;
    
    /** Tipo de artículo: "desayuno" o "bebida" */
    private String tipo;
    
    /** Precio unitario del artículo */
    private double precioUnitario;
    
    /** URL de la imagen del artículo almacenada en Firebase Storage */
    private String imagenUrl;

    /**
     * Constructor vacío requerido por Firestore para la deserialización.
     */
    public Articulo() {
        // Constructor vacío para Firestore
    }

    /**
     * Constructor con parámetros para crear un nuevo artículo.
     * 
     * @param nombre Nombre del artículo
     * @param tipo Tipo de artículo ("desayuno" o "bebida")
     * @param precioUnitario Precio unitario del artículo
     * @param imagenUrl URL de la imagen del artículo
     */
    public Articulo(String nombre, String tipo, double precioUnitario, String imagenUrl) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioUnitario = precioUnitario;
        this.imagenUrl = imagenUrl;
    }

    // Getters y Setters

    /**
     * Obtiene el ID del artículo.
     * 
     * @return ID del artículo
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID del artículo.
     * 
     * @param id ID del artículo
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del artículo.
     * 
     * @return Nombre del artículo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del artículo.
     * 
     * @param nombre Nombre del artículo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el tipo del artículo.
     * 
     * @return Tipo del artículo ("desayuno" o "bebida")
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo del artículo.
     * 
     * @param tipo Tipo del artículo ("desayuno" o "bebida")
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el precio unitario del artículo.
     * 
     * @return Precio unitario del artículo
     */
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario del artículo.
     * 
     * @param precioUnitario Precio unitario del artículo
     */
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Obtiene la URL de la imagen del artículo.
     * 
     * @return URL de la imagen del artículo
     */
    public String getImagenUrl() {
        return imagenUrl;
    }

    /**
     * Establece la URL de la imagen del artículo.
     * 
     * @param imagenUrl URL de la imagen del artículo
     */
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
