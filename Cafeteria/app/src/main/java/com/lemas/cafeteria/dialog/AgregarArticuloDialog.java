package com.lemas.cafeteria.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lemas.cafeteria.R;
import com.lemas.cafeteria.model.Articulo;
import com.lemas.cafeteria.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Diálogo para agregar un nuevo artículo (desayuno o bebida).
 * Permite ingresar nombre, precio y seleccionar una imagen.
 * La imagen se sube a Firebase Storage y el artículo se guarda en Firestore.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class AgregarArticuloDialog extends DialogFragment {

    /** Launcher para seleccionar imagen desde la galería */
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    
    /** Campo de texto para el nombre del artículo */
    private EditText etNombre;
    
    /** Campo de texto para el precio del artículo */
    private EditText etPrecio;
    
    /** Vista previa de la imagen seleccionada */
    private ImageView imagenPreview;
    
    /** Botones del diálogo */
    private Button btnSeleccionarImagen, btnGuardar, btnCancelar;
    
    /** URI de la imagen seleccionada */
    private Uri imagenUri;
    
    /** Tipo de artículo: "desayuno" o "bebida" */
    private String tipoArticulo;
    
    /** Listener para notificar cuando se guarda un artículo */
    private OnArticuloGuardadoListener listener;

    /**
     * Interfaz para recibir notificaciones cuando se guarda un artículo.
     */
    public interface OnArticuloGuardadoListener {
        /**
         * Se ejecuta cuando un artículo se guarda exitosamente.
         */
        void onArticuloGuardado();
    }

    /**
     * Crea una nueva instancia del diálogo con el tipo de artículo especificado.
     * 
     * @param tipoArticulo Tipo de artículo ("desayuno" o "bebida")
     * @return Nueva instancia del diálogo
     */
    public static AgregarArticuloDialog newInstance(String tipoArticulo) {
        AgregarArticuloDialog dialog = new AgregarArticuloDialog();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_TIPO_ARTICULO, tipoArticulo);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * Establece el listener para recibir notificaciones cuando se guarda un artículo.
     * 
     * @param listener Listener a establecer
     */
    public void setOnArticuloGuardadoListener(OnArticuloGuardadoListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Registrar el launcher para seleccionar imagen
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null && result.getData().getData() != null) {
                    imagenUri = result.getData().getData();
                    mostrarImagenPreview();
                }
            }
        );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Obtener el tipo de artículo de los argumentos
        if (getArguments() != null) {
            tipoArticulo = getArguments().getString(Constants.ARG_TIPO_ARTICULO, Constants.TIPO_DESAYUNO);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_agregar_articulo, null);

        inicializarVistas(view);
        configurarListeners();

        builder.setView(view);
        builder.setTitle(R.string.agregar_articulo);

        return builder.create();
    }

    /**
     * Inicializa las vistas del diálogo.
     * 
     * @param view Vista raíz del diálogo
     */
    private void inicializarVistas(View view) {
        etNombre = view.findViewById(R.id.et_nombre);
        etPrecio = view.findViewById(R.id.et_precio);
        imagenPreview = view.findViewById(R.id.imagen_preview);
        btnSeleccionarImagen = view.findViewById(R.id.btn_seleccionar_imagen);
        btnGuardar = view.findViewById(R.id.btn_guardar);
        btnCancelar = view.findViewById(R.id.btn_cancelar);
    }

    /**
     * Configura los listeners de los botones.
     */
    private void configurarListeners() {
        btnSeleccionarImagen.setOnClickListener(v -> abrirSelectorImagen());
        btnGuardar.setOnClickListener(v -> guardarArticulo());
        btnCancelar.setOnClickListener(v -> dismiss());
    }

    /**
     * Abre el selector de imágenes de la galería.
     */
    private void abrirSelectorImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, getString(R.string.seleccionar_imagen)));
    }

    /**
     * Muestra la imagen seleccionada en la vista previa.
     */
    private void mostrarImagenPreview() {
        if (imagenUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().getContentResolver(), imagenUri);
                Glide.with(this).load(bitmap).into(imagenPreview);
            } catch (IOException e) {
                mostrarError(getString(R.string.error_procesar_imagen));
            }
        }
    }

    /**
     * Valida y guarda el artículo en Firestore.
     */
    private void guardarArticulo() {
        String nombre = etNombre.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        // Validar campos
        if (!validarCampos(nombre, precioStr)) {
            return;
        }

        double precio = Double.parseDouble(precioStr);

        // Subir imagen si existe, luego guardar artículo
        if (imagenUri != null) {
            subirImagenYGuardarArticulo(nombre, precio);
        } else {
            guardarArticuloEnFirestore(nombre, precio, "");
        }
    }

    /**
     * Valida que los campos estén completos y sean válidos.
     * 
     * @param nombre Nombre del artículo
     * @param precioStr Precio como string
     * @return true si los campos son válidos, false en caso contrario
     */
    private boolean validarCampos(String nombre, String precioStr) {
        if (nombre.isEmpty() || precioStr.isEmpty()) {
            mostrarError(getString(R.string.error_campos_vacios));
            return false;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            if (precio < Constants.PRECIO_MINIMO) {
                mostrarError(getString(R.string.error_precio_invalido));
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError(getString(R.string.error_precio_invalido));
            return false;
        }

        return true;
    }

    /**
     * Sube la imagen a Firebase Storage y luego guarda el artículo en Firestore.
     * 
     * @param nombre Nombre del artículo
     * @param precio Precio del artículo
     */
    private void subirImagenYGuardarArticulo(String nombre, double precio) {
        Toast.makeText(requireContext(), R.string.subiendo_imagen, Toast.LENGTH_SHORT).show();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String nombreArchivo = UUID.randomUUID().toString() + Constants.EXTENSION_IMAGEN;
        StorageReference imagenRef = storageRef.child(Constants.STORAGE_PATH_ARTICULOS + nombreArchivo);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().getContentResolver(), imagenUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.CALIDAD_IMAGEN_JPEG, baos);
            byte[] data = baos.toByteArray();

            imagenRef.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        imagenRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> guardarArticuloEnFirestore(nombre, precio, uri.toString()))
                                .addOnFailureListener(e -> mostrarError(getString(R.string.error_obtener_url_imagen)));
                    })
                    .addOnFailureListener(e -> mostrarError(getString(R.string.error_subir_imagen)));

        } catch (IOException e) {
            mostrarError(getString(R.string.error_procesar_imagen));
        }
    }

    /**
     * Guarda el artículo en Firestore.
     * 
     * @param nombre Nombre del artículo
     * @param precio Precio del artículo
     * @param imagenUrl URL de la imagen del artículo
     */
    private void guardarArticuloEnFirestore(String nombre, double precio, String imagenUrl) {
        Toast.makeText(requireContext(), R.string.guardando_articulo, Toast.LENGTH_SHORT).show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Articulo articulo = new Articulo(nombre, tipoArticulo, precio, imagenUrl);

        db.collection(Constants.COLLECTION_ARTICULOS)
                .add(articulo)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(), 
                            R.string.articulo_guardado_exitosamente, Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onArticuloGuardado();
                    }
                    dismiss();
                })
                .addOnFailureListener(e -> mostrarError(getString(R.string.error_guardar_articulo)));
    }

    /**
     * Muestra un mensaje de error.
     * 
     * @param mensaje Mensaje de error a mostrar
     */
    private void mostrarError(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
