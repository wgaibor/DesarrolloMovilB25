package com.lemas.cafeteria.fragment;

import androidx.annotation.NonNull;
import com.lemas.cafeteria.R;
import com.lemas.cafeteria.util.Constants;

/**
 * Fragment que muestra la lista de desayunos disponibles.
 * Permite agregar nuevos desayunos y agregarlos al carrito.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class DesayunosFragment extends BaseArticuloFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_desayunos;
    }

    @NonNull
    @Override
    protected String getTipoArticulo() {
        return Constants.TIPO_DESAYUNO;
    }
}
