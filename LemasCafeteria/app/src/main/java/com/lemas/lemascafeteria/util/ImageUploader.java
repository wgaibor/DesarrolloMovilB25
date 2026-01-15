package com.lemas.lemascafeteria.util;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Utilidad para subir imágenes a ImgBB (servicio gratuito de almacenamiento de imágenes).
 *
 * ImgBB ofrece almacenamiento gratuito de imágenes con las siguientes características:
 * - Completamente gratuito
 * - No requiere registro ni API key para uso básico
 * - URLs directas a las imágenes
 * - Sin límite de almacenamiento (con límites razonables de uso)
 *
 * Documentación: https://api.imgbb.com/
 *
 * @author Cafeteria App
 * @version 1.0
 */
public class ImageUploader {
    private static final MediaType IMAGE_JPEG = MediaType.parse("image/jpeg");
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * API Key de ImgBB (opcional, pero recomendado para mejor rendimiento).
     * Puedes obtener una API key gratuita en: https://api.imgbb.com/
     * Si no proporcionas una API key, ImgBB funcionará pero con límites más estrictos.
     *
     * Para obtener una API key gratuita:
     * 1. Ve a https://api.imgbb.com/
     * 2. Haz clic en "Get API Key"
     * 3. Completa el formulario (es gratis)
     * 4. Copia tu API key
     * 5. Reemplaza "TU_API_KEY_AQUI" con tu API key real (o déjalo vacío para usar sin API key)
     */
    private static final String IMGBB_API_KEY = "3d0250110a6fe1e0b5556bd222f24629"; // Opcional: puedes dejar vacío o poner tu API key

    /**
     * URL del endpoint de ImgBB para subir imágenes
     */
    private static final String IMGBB_UPLOAD_URL = "https://api.imgbb.com/1/upload";

    /**
     * Interfaz para recibir el resultado de la subida de imagen.
     */
    public interface UploadCallback {
        /**
         * Se ejecuta cuando la imagen se sube exitosamente.
         *
         * @param imageUrl URL de la imagen subida
         */
        void onSuccess(String imageUrl);

        /**
         * Se ejecuta cuando ocurre un error al subir la imagen.
         *
         * @param error Mensaje de error
         */
        void onError(String error);
    }


    /**
     * Sube una imagen a ImgBB.
     *
     * @param bitmap Bitmap de la imagen a subir
     * @param callback Callback para recibir el resultado
     */
    public static void uploadImage(@NonNull Bitmap bitmap, @NonNull UploadCallback callback) {
        try {
            // Convertir bitmap a bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
            byte[] imageBytes = baos.toByteArray();

            // Crear el cuerpo de la petición multipart
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "image.jpg",
                            RequestBody.create(imageBytes, IMAGE_JPEG));

            // Agregar API key si está configurada
            if (IMGBB_API_KEY != null && !IMGBB_API_KEY.isEmpty()) {
                requestBodyBuilder.addFormDataPart("key", IMGBB_API_KEY);
            }

            RequestBody requestBody = requestBodyBuilder.build();

            // Crear la petición
            Request request = new Request.Builder()
                    .url(IMGBB_UPLOAD_URL)
                    .post(requestBody)
                    .build();

            // Ejecutar la petición de forma asíncrona
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onError("Error de conexión: " + e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        String errorBody = response.body() != null ? response.body().string() : "Error desconocido";
                        callback.onError("Error al subir imagen: " + response.code() + " - " + errorBody);
                        return;
                    }

                    try {
                        String responseBody = response.body() != null ? response.body().string() : "";
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        if (jsonResponse.has("success") && jsonResponse.getBoolean("success")) {
                            JSONObject data = jsonResponse.getJSONObject("data");
                            String imageUrl = data.getString("url");
                            callback.onSuccess(imageUrl);
                        } else {
                            String errorMsg = jsonResponse.optJSONObject("error") != null
                                    ? jsonResponse.getJSONObject("error").optString("message", "Error desconocido")
                                    : "Error al subir imagen";
                            callback.onError(errorMsg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("Error al procesar respuesta: " + e.getMessage());
                    }
                }
            });


        } catch (Exception e) {
            callback.onError("Error al crear la petición: " + e.getMessage());
        }
    }
}
