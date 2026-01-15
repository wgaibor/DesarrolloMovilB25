package com.lemas.lemascafeteria.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.lemas.lemascafeteria.R;
import com.lemas.lemascafeteria.model.Articulo;
import com.lemas.lemascafeteria.util.Constantes;
import com.lemas.lemascafeteria.util.ImageUploader;

import java.io.IOException;

public class AgregarArticuloDialog extends DialogFragment {
    /** Launcher para seleccionar imagen desde la galería */
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    /** Tipo de artículo: "desayuno" o "bebida" */
    private String tipoArticulo;

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

    public static AgregarArticuloDialog newInstance(String tipoArticulo) {
        Bundle args = new Bundle();
        args.putString(Constantes.ARG_TIPO_ARTICULO, tipoArticulo);
        AgregarArticuloDialog fragment = new AgregarArticuloDialog();
        fragment.setArguments(args);
        return fragment;
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
                } );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if(getArguments() != null) {
            tipoArticulo = getArguments().getString(Constantes.ARG_TIPO_ARTICULO, Constantes.TIPO_DESAYUNO);
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

    private void configurarListeners() {
        btnSeleccionarImagen.setOnClickListener(v -> abrirSelectorImagen());
        btnGuardar.setOnClickListener(v -> guardarArticulo());
        btnCancelar.setOnClickListener(v -> dismiss());
    }

    private void guardarArticulo() {
        String nombre = etNombre.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();

        if(!validarCampos(nombre, precio)) {
            return;
        }

        double precioUnitario = Double.parseDouble(precio);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imagenUri);

            // Subir imagen a ImgBB
            ImageUploader.uploadImage(bitmap, new ImageUploader.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    guardarItemEnFirestore(nombre, precioUnitario, imageUrl);
                }

                @Override
                public void onError(String error) {
                    Log.d("XXXXXXX", error);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda el artículo en Firestore.
     *
     * @param nombre Nombre del artículo
     * @param precio Precio del artículo
     * @param imageUrl URL de la imagen del artículo
     */
    private void guardarItemEnFirestore(String nombre, double precio, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Articulo articulo = new Articulo(nombre, tipoArticulo, precio, imageUrl);

        db.collection(Constantes.COLLECTION_ARTICULOS)
                .add(articulo)
                .addOnSuccessListener( documentReference -> {
                    Toast.makeText(requireContext(), R.string.articulo_guardado_exitosamente, Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .addOnFailureListener( e -> mostrarError(getString(R.string.error_guardar_articulo)));
    }

    private boolean validarCampos(String nombre, String precioStr) {
        if (nombre.isEmpty() || precioStr.isEmpty()) {
            mostrarError(getString(R.string.error_campos_vacios));
            return false;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            if (precio < Constantes.PRECIO_MINIMO) {
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
     * Abre el selector de imágenes de la galería.
     */
    private void abrirSelectorImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Seleccionar Imagen"));
    }

    /**
     * Muestra la imagen seleccionada en la vista previa.
     */
    private void mostrarImagenPreview() {
        if (imagenUri != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().getContentResolver(), imagenUri
                );
                Glide.with(this).load(bitmap).into(imagenPreview);
            } catch (IOException e) {
                mostrarError(getString(R.string.error_procesar_imagen));
            }
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }


    private void inicializarVistas(View view) {
        etNombre = view.findViewById(R.id.et_nombre);
        etPrecio = view.findViewById(R.id.et_precio);
        imagenPreview = view.findViewById(R.id.imagen_preview);
        btnSeleccionarImagen = view.findViewById(R.id.btn_seleccionar_imagen);
        btnCancelar = view.findViewById(R.id.btn_cancelar);
        btnGuardar = view.findViewById(R.id.btn_guardar);
    }
}
