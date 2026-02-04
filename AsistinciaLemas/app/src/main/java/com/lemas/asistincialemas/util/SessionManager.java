package com.lemas.asistincialemas.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.lemas.asistincialemas.model.UsuarioResponse;

public class SessionManager {

    private static final String PREF_NAME = "AsistenciaSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_CEDULA = "user_cedula";
    private static final String KEY_USER_NOMBRE = "user_nombre";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ROL = "user_rol";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveSession(String token, UsuarioResponse usuario) {
        editor.putString(KEY_TOKEN, token);
        editor.putLong(KEY_USER_ID, usuario.getId());
        editor.putString(KEY_USER_CEDULA, usuario.getCedula());
        editor.putString(KEY_USER_NOMBRE, usuario.getNombre());
        editor.putString(KEY_USER_EMAIL, usuario.getEmail());
        editor.putString(KEY_USER_ROL, usuario.getRol());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public Long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1);
    }

    public String getUserCedula() {
        return prefs.getString(KEY_USER_CEDULA, null);
    }

    public String getUserNombre() {
        return prefs.getString(KEY_USER_NOMBRE, null);
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public String getUserRol() {
        return prefs.getString(KEY_USER_ROL, null);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
