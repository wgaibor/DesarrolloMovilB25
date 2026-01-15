package com.lemas.lemascafeteria.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lemas.lemascafeteria.R;
import com.lemas.lemascafeteria.util.Constantes;

public class BebidasFragment extends BaseArticuloFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_bebidas;
    }

    @Override
    protected String getTipoArticulo() {
        return Constantes.TIPO_BEBIDA;
    }
}