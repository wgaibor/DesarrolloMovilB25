package com.lemas.cafeteria.util;

/**
 * Clase de constantes utilizadas en toda la aplicación.
 * Centraliza los valores constantes para facilitar el mantenimiento.
 */
public class Constants {
    
    /** Nombre de la colección de artículos en Firestore */
    public static final String COLLECTION_ARTICULOS = "articulos";
    
    /** Campo tipo para artículos de desayuno */
    public static final String TIPO_DESAYUNO = "desayuno";
    
    /** Campo tipo para artículos de bebida */
    public static final String TIPO_BEBIDA = "bebida";
    
    /** Ruta base para almacenar imágenes en Firebase Storage */
    public static final String STORAGE_PATH_ARTICULOS = "articulos/";
    
    /** Formato de precio con 2 decimales */
    public static final String FORMATO_PRECIO = "$%.2f";
    
    /** Calidad de compresión de imagen JPEG */
    public static final int CALIDAD_IMAGEN_JPEG = 80;
    
    /** Extensión de archivo de imagen */
    public static final String EXTENSION_IMAGEN = ".jpg";
    
    /** Tag para el diálogo de agregar artículo */
    public static final String TAG_DIALOG_AGREGAR = "AgregarArticuloDialog";
    
    /** Argumento para pasar el tipo de artículo al diálogo */
    public static final String ARG_TIPO_ARTICULO = "tipo";
    
    /** Cantidad mínima de artículos en el carrito */
    public static final int CANTIDAD_MINIMA = 1;
    
    /** Precio mínimo válido para un artículo */
    public static final double PRECIO_MINIMO = 0.01;
    
    private Constants() {
        // Prevenir instanciación
    }
}
