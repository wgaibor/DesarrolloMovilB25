package com.lemas.lemascafeteria.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lemas.lemascafeteria.R;
import com.lemas.lemascafeteria.dialog.AgregarArticuloDialog;
import com.lemas.lemascafeteria.model.Articulo;
import com.lemas.lemascafeteria.util.Constantes;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseArticuloFragment extends Fragment {

    /** Lista de artículos cargados desde Firestore */
    protected List<Articulo> listaArticulos;

    /** Instancia de Firestore para consultas */
    protected FirebaseFirestore db;

    /** RecyclerView para mostrar la lista de artículos */
    protected RecyclerView recyclerArticulos;

    /** TextView que se muestra cuando no hay artículos */
    protected TextView tvSinArticulos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        listaArticulos = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarVistas(view);
        configurarRecyclerView();
        //cargarArticulos();
    }

    private void configurarRecyclerView() {

    }

    private void inicializarVistas(View view) {
        recyclerArticulos = view.findViewById(R.id.recycler_articulos);
        tvSinArticulos = view.findViewById(R.id.tv_sin_articulos);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_agregar) {
            mostrarDialogoAgregar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogoAgregar() {
        AgregarArticuloDialog dialog = AgregarArticuloDialog.newInstance(getTipoArticulo());
        dialog.show(getParentFragmentManager(), Constantes.TAG_DIALOG_AGREGAR);
    }

    /**
     * Obtiene el ID del layout del fragment.
     * Debe ser implementado por las clases hijas.
     *
     * @return ID del recurso de layout
     */
    protected abstract int getLayoutResId();

    protected abstract String getTipoArticulo();
}
