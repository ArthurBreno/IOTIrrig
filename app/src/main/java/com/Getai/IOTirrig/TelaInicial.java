package com.Getai.IOTirrig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class TelaInicial extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    // ------------------------------------------------------------------------------------------------------//
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.telaInicial);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // ------------------------------------------------------------------------------------------------------//
        Button btnControle = findViewById(R.id.btnControle);
        btnControle.setOnClickListener(v -> {
            Intent intent = new Intent(TelaInicial.this, AtividadeControlar.class);
            startActivity(intent);
        });
        Button btnMonitormaento = findViewById(R.id.btnMonitoramento);
        btnMonitormaento.setOnClickListener(v -> {
            Intent intent = new Intent(TelaInicial.this, AtividadeMonitorar.class);
            startActivity(intent);
        });
    }
    // ------------------------------------------------------------------------------------------------------//
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_inf:
                Intent intent = new Intent(getApplicationContext(),TelaInicial.class);
                startActivity(intent);
                break;
            case R.id.nav_inf2:
                Intent intent2 = new Intent(getApplicationContext(),AtividadeControlar.class);
                startActivity(intent2);
                break;
            case R.id.nav_inf3:
                Intent intent3 = new Intent(getApplicationContext(),AtividadeMonitorar.class);
                startActivity(intent3);
                break;
            case R.id.instrucoes:
                Intent intent5 = new Intent(getApplicationContext(),Instruicoes.class);
                startActivity(intent5);
                break;
            case R.id.nav_inf4:
                Intent intent4 = new Intent(getApplicationContext(),AtividadeSobre.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}