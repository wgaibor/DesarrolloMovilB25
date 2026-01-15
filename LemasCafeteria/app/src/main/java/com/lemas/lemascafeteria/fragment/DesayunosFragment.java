package com.lemas.lemascafeteria.fragment;

import com.lemas.lemascafeteria.R;
import com.lemas.lemascafeteria.util.Constantes;

public class DesayunosFragment extends BaseArticuloFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_desayunos;
    }

    @Override
    protected String getTipoArticulo() {
        return Constantes.TIPO_DESAYUNO;
    }
}