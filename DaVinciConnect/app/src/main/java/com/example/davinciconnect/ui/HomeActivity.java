package com.example.davinciconnect.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.davinciconnect.R;
import com.example.davinciconnect.ui.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Toggle para abrir/cerrar drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Cargar fragment por defecto (por ejemplo, menú principal)
        if (savedInstanceState == null) {
            loadFragment(new ProfileFragment()); // puedes poner otro si quieres
            navigationView.setCheckedItem(R.id.nav_profile);
        }

        // Opciones del menú hamburguesa
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                int id = item.getItemId();
                if (id == R.id.nav_profile) {
                    fragment = new ProfileFragment();
                } else if (id == R.id.nav_darkmode) {
                    // luego conectamos con SettingsFragment
                } else if (id == R.id.nav_comments) {
                    // CommentsFragment
                } else if (id == R.id.nav_contact) {
                    // ContactFragment
                }

                if (fragment != null) {
                    loadFragment(fragment);
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
