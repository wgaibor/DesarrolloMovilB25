package com.lemas.cafeteria.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lemas.cafeteria.R;
import com.lemas.cafeteria.adapter.ArticuloAdapter;
import com.lemas.cafeteria.dialog.AgregarArticuloDialog;
import com.lemas.cafeteria.model.Articulo;
import com.lemas.cafeteria.util.CarritoManager;
import com.lemas.cafeteria.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment base para mostrar listas de artículos (Desayunos o Bebidas).
 * Proporciona funcionalidad común para ambos tipos de fragmentos.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public abstract class BaseArticuloFragment extends Fragment {

    /** RecyclerView para mostrar la lista de artículos */
    protected RecyclerView recyclerArticulos;
    
    /** TextView que se muestra cuando no hay artículos */
    protected TextView tvSinArticulos;
    
    /** Adapter para el RecyclerView */
    protected ArticuloAdapter adapter;
    
    /** Lista de artículos cargados desde Firestore */
    protected List<Articulo> listaArticulos;
    
    /** Instancia de Firestore para consultas */
    protected FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = FirebaseFirestore.getInstance();
        listaArticulos = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, 
                             @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inicializarVistas(view);
        configurarRecyclerView();
        cargarArticulos();
    }

    /**
     * Inicializa las vistas del fragment.
     * 
     * @param view Vista raíz del fragment
     */
    protected void inicializarVistas(View view) {
        recyclerArticulos = view.findViewById(R.id.recycler_articulos);
        tvSinArticulos = view.findViewById(R.id.tv_sin_articulos);
    }

    /**
     * Configura el RecyclerView con su adapter y layout manager.
     */
    protected void configurarRecyclerView() {
        recyclerArticulos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ArticuloAdapter(listaArticulos, this::onArticuloClick);
        recyclerArticulos.setAdapter(adapter);
    }

    /**
     * Maneja el click en un artículo, agregándolo al carrito.
     * 
     * @param articulo Artículo seleccionado
     */
    protected void onArticuloClick(Articulo articulo) {
        CarritoManager.getInstance().agregarArticulo(articulo);
        Toast.makeText(getContext(), R.string.articulo_agregado, Toast.LENGTH_SHORT).show();
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

    /**
     * Muestra el diálogo para agregar un nuevo artículo.
     */
    protected void mostrarDialogoAgregar() {
        AgregarArticuloDialog dialog = AgregarArticuloDialog.newInstance(getTipoArticulo());
        dialog.setOnArticuloGuardadoListener(this::cargarArticulos);
        dialog.show(getParentFragmentManager(), Constants.TAG_DIALOG_AGREGAR);
    }

    /**
     * Carga los artículos desde Firestore según el tipo.
     */
    protected void cargarArticulos() {
        db.collection(Constants.COLLECTION_ARTICULOS)
                .whereEqualTo("tipo", getTipoArticulo())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        listaArticulos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Articulo articulo = document.toObject(Articulo.class);
                            articulo.setId(document.getId());
                            listaArticulos.add(articulo);
                        }
                        adapter.updateList(listaArticulos);
                        actualizarVistaVacia();
                    } else {
                        mostrarErrorCarga(task.getException());
                    }
                });
    }

    /**
     * Muestra un mensaje de error al cargar los artículos.
     * 
     * @param exception Excepción ocurrida durante la carga
     */
    protected void mostrarErrorCarga(Exception exception) {
        String mensaje = getString(R.string.error_cargar_articulos);
        if (exception != null && exception.getMessage() != null) {
            mensaje += ": " + exception.getMessage();
        }
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    /**
     * Actualiza la visibilidad de las vistas según si hay artículos o no.
     */
    protected void actualizarVistaVacia() {
        if (listaArticulos.isEmpty()) {
            recyclerArticulos.setVisibility(View.GONE);
            tvSinArticulos.setVisibility(View.VISIBLE);
        } else {
            recyclerArticulos.setVisibility(View.VISIBLE);
            tvSinArticulos.setVisibility(View.GONE);
        }
    }

    /**
     * Obtiene el ID del layout del fragment.
     * Debe ser implementado por las clases hijas.
     * 
     * @return ID del recurso de layout
     */
    protected abstract int getLayoutResId();

    /**
     * Obtiene el tipo de artículo que maneja este fragment.
     * Debe ser implementado por las clases hijas.
     * 
     * @return Tipo de artículo ("desayuno" o "bebida")
     */
    protected abstract String getTipoArticulo();
}
