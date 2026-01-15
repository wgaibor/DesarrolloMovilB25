package com.lemas.lemascafeteria.activity;

import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.lemas.lemascafeteria.R;
import com.lemas.lemascafeteria.fragment.AcercaDeFragment;
import com.lemas.lemascafeteria.fragment.BebidasFragment;
import com.lemas.lemascafeteria.fragment.DesayunosFragment;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Configurar toolbar
        configurarToolbar();
        configurarDrawerLayout();
        configurarNavigationView();
    }

    private void configurarNavigationView() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configurarDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configurarToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = obtenerFragmentPorItemId(menuItem.getItemId());
        if(fragment != null){
            loadFragment(fragment);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private Fragment obtenerFragmentPorItemId(int itemId) {
        if (itemId == R.id.nav_desayunos){
            return new DesayunosFragment();
        } else if (itemId == R.id.nav_bebidas) {
            return new BebidasFragment();
        } else if (itemId == R.id.nav_acerca_de){
            return new AcercaDeFragment();
        }
        return null;
    }
}