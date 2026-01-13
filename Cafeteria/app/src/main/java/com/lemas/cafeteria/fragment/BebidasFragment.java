package com.lemas.cafeteria.fragment;

import androidx.annotation.NonNull;
import com.lemas.cafeteria.R;
import com.lemas.cafeteria.util.Constants;

/**
 * Fragment que muestra la lista de bebidas disponibles.
 * Permite agregar nuevas bebidas y agregarlas al carrito.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class BebidasFragment extends BaseArticuloFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_bebidas;
    }

    @NonNull
    @Override
    protected String getTipoArticulo() {
        return Constants.TIPO_BEBIDA;
    }
}
