package com.lemas.cafeteria.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lemas.cafeteria.R;
import com.lemas.cafeteria.adapter.CarritoAdapter;
import com.lemas.cafeteria.util.CarritoManager;
import com.lemas.cafeteria.util.Constants;

/**
 * Actividad que muestra el carrito de compras.
 * Permite ver los artículos agregados, modificar cantidades,
 * eliminar items y procesar el pago.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class CarritoActivity extends AppCompatActivity {

    /** RecyclerView para mostrar los items del carrito */
    private RecyclerView recyclerCarrito;
    
    /** TextView que muestra el total del carrito */
    private TextView tvTotal;
    
    /** Botón para procesar el pago */
    private Button btnPagar;
    
    /** Adapter para el RecyclerView */
    private CarritoAdapter adapter;
    
    /** Instancia del gestor del carrito */
    private CarritoManager carritoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        configurarToolbar();
        inicializarComponentes();
        configurarRecyclerView();
        configurarListeners();
        actualizarVista();
    }

    /**
     * Configura la toolbar de la actividad.
     */
    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.carrito);
        }
    }

    /**
     * Inicializa los componentes de la actividad.
     */
    private void inicializarComponentes() {
        carritoManager = CarritoManager.getInstance();
        recyclerCarrito = findViewById(R.id.recycler_carrito);
        tvTotal = findViewById(R.id.tv_total);
        btnPagar = findViewById(R.id.btn_pagar);
    }

    /**
     * Configura el RecyclerView con su adapter y layout manager.
     */
    private void configurarRecyclerView() {
        recyclerCarrito.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarritoAdapter(
                carritoManager.getItems(),
                this::actualizarTotal,
                this::actualizarLista
        );
        recyclerCarrito.setAdapter(adapter);
    }

    /**
     * Configura los listeners de los componentes.
     */
    private void configurarListeners() {
        btnPagar.setOnClickListener(v -> procesarPago());
    }

    /**
     * Actualiza la vista con los datos actuales del carrito.
     */
    private void actualizarVista() {
        actualizarLista();
        actualizarTotal();
    }

    /**
     * Actualiza el total del carrito en el TextView.
     */
    private void actualizarTotal() {
        double total = carritoManager.getTotal();
        String textoTotal = getString(R.string.total) + ": " + 
                String.format(Constants.FORMATO_PRECIO, total);
        tvTotal.setText(textoTotal);
    }

    /**
     * Actualiza la lista de items del carrito en el adapter.
     */
    private void actualizarLista() {
        adapter.updateList(carritoManager.getItems());
        actualizarTotal();
    }

    /**
     * Procesa el pago del carrito.
     * Valida que el carrito no esté vacío y muestra un mensaje de confirmación.
     */
    private void procesarPago() {
        if (carritoManager.estaVacio()) {
            Toast.makeText(this, R.string.carrito_vacio, Toast.LENGTH_SHORT).show();
            return;
        }

        double total = carritoManager.getTotal();
        String mensaje = "Procesando pago por " + String.format(Constants.FORMATO_PRECIO, total);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        
        // Limpiar el carrito y cerrar la actividad
        carritoManager.limpiarCarrito();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
