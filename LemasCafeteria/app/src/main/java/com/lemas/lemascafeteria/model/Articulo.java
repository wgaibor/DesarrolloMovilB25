package com.lemas.lemascafeteria.model;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
