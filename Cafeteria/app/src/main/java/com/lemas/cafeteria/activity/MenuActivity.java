package com.lemas.cafeteria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.lemas.cafeteria.R;
import com.lemas.cafeteria.fragment.AcercaDeFragment;
import com.lemas.cafeteria.fragment.BebidasFragment;
import com.lemas.cafeteria.fragment.DesayunosFragment;

/**
 * Actividad principal que contiene el menú de navegación lateral (Drawer)
 * y gestiona la navegación entre los diferentes fragments de la aplicación.
 * 
 * @author Cafeteria App
 * @version 1.0
 */
public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** Layout del menú lateral */
    private DrawerLayout drawerLayout;
    
    /** Vista de navegación lateral */
    private NavigationView navigationView;
    
    /** Barra de herramientas */
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        configurarToolbar();
        configurarDrawerLayout();
        configurarNavigationView();

        // Mostrar fragmento por defecto (Desayunos)
        if (savedInstanceState == null) {
            cargarFragmentoInicial();
        }
    }

    /**
     * Configura la toolbar de la actividad.
     */
    private void configurarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Configura el DrawerLayout y su toggle.
     */
    private void configurarDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Configura el NavigationView y su listener.
     */
    private void configurarNavigationView() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Carga el fragmento inicial (Desayunos).
     */
    private void cargarFragmentoInicial() {
        loadFragment(new DesayunosFragment());
        navigationView.setCheckedItem(R.id.nav_desayunos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_carrito) {
            abrirCarrito();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Abre la actividad del carrito de compras.
     */
    private void abrirCarrito() {
        Intent intent = new Intent(this, CarritoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = obtenerFragmentoPorItemId(item.getItemId());

        if (fragment != null) {
            loadFragment(fragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Obtiene el fragmento correspondiente según el ID del item del menú.
     * 
     * @param itemId ID del item del menú seleccionado
     * @return Fragment correspondiente o null si no hay coincidencia
     */
    private Fragment obtenerFragmentoPorItemId(int itemId) {
        if (itemId == R.id.nav_desayunos) {
            return new DesayunosFragment();
        } else if (itemId == R.id.nav_bebidas) {
            return new BebidasFragment();
        } else if (itemId == R.id.nav_acerca_de) {
            return new AcercaDeFragment();
        }
        return null;
    }

    /**
     * Carga un fragmento en el contenedor principal.
     * 
     * @param fragment Fragment a cargar
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
