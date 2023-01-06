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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AtividadeCriarControle extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    int numero;
    // ------------------------------------------------------------------------------------------------------//
    String[] vetorEnviarDadosControle = new String[5];
    // ------------------------------------------------------------------------------------------------------//
    public ArrayList<EditText> listaEdTxt = new ArrayList<>();
    // ------------------------------------------------------------------------------------------------------//
    EditText edTituloControle, edUrlLeituraControle, edUrlEscritaControle;
    Button btnSalvarCriarControle;
    Spinner spinner;
    // ------------------------------------------------------------------------------------------------------//
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_criar_controle);
        drawerLayout = findViewById(R.id.atividadeCriarControle);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // ------------------------------------------------------------------------------------------------------//
        edTituloControle = findViewById(R.id.edTituloControle);
        edUrlLeituraControle = findViewById(R.id.edUrlLeituraControle);
        edUrlEscritaControle = findViewById(R.id.edUrlEscritaControle);
        btnSalvarCriarControle = findViewById(R.id.btnSalvarCriarControle);
        spinner = findViewById(R.id.spinnerControlar);
        // ------------------------------------------------------------------------------------------------------//
        listaEdTxt.add(edTituloControle);
        listaEdTxt.add(edUrlEscritaControle);
        listaEdTxt.add(edUrlLeituraControle);
        // ------------------------------------------------------------------------------------------------------//
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.campos, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numero = position + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // ------------------------------------------------------------------------------------------------------//
        btnSalvarCriarControle.setOnClickListener(v -> {
            enviarDadosControles();
        });
        receberDadosModificadosControle();
    }

    private void receberDadosModificadosControle() {
        Intent intent = getIntent();
        String[] receberDadosModificadosControle = intent.getStringArrayExtra("vetorModificarCriar");
        if (receberDadosModificadosControle != null){
            edTituloControle.setText(receberDadosModificadosControle[0]);
            edUrlEscritaControle.setText(receberDadosModificadosControle[1]);
            edUrlLeituraControle.setText(receberDadosModificadosControle[2]);
            spinner.setSelection(Integer.parseInt(receberDadosModificadosControle[3]) - 1);
        }
    }

    public boolean checarVazios(){
        boolean check = false;
        int verificador = 6;
        if (edUrlLeituraControle.getText().toString().isEmpty()) {
            verificador = 3;
        }
        if (edUrlEscritaControle.getText().toString().isEmpty()) {
            verificador = 2;
        }
        if (edTituloControle.getText().toString().isEmpty()){
            verificador = 1;
        }
        switch (verificador){
            case 1:
                Toast.makeText(getApplicationContext(), "PREECHER TITULO", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "PREECHER URL PARA ESCRITA", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "PREECHER URL PARA LEITURA", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                check = true;
                break;
        }
        return check;
    }

        public void enviarDadosControles() {
            if (checarVazios()) {
                Intent intent = getIntent();
                String[] receberVetorModifcarControlar = intent.getStringArrayExtra("vetorModificarCriar");
                if (receberVetorModifcarControlar != null) {
                    String[] vetorEnviarModifcadoControle = new String[5];
                //    for (int i = 0; i < listaEdTxt.size() - 1; i++) {
                //        vetorEnviarModifcadoControle[i] = listaEdTxt.get(i).getText().toString();
                //    }
                    vetorEnviarModifcadoControle[0] = edTituloControle.getText().toString();
                    vetorEnviarModifcadoControle[1] = edUrlEscritaControle.getText().toString();
                    vetorEnviarModifcadoControle[2] = edUrlLeituraControle.getText().toString();
                    vetorEnviarModifcadoControle[3] = Integer.toString(numero);
                    vetorEnviarModifcadoControle[4] = receberVetorModifcarControlar[4];
                    Intent intent1 = new Intent(AtividadeCriarControle.this, AtividadeControlar.class);
                    intent1.putExtra("vetorReceberDadosModificadosControle", vetorEnviarModifcadoControle);
                    startActivity(intent1);
                } else {
                    for (int i = 0; i < listaEdTxt.size(); i++) {
                        vetorEnviarDadosControle[i] = listaEdTxt.get(i).getText().toString();
                    }
                    vetorEnviarDadosControle[3] = Integer.toString(numero);
                    Intent intent2 = new Intent(AtividadeCriarControle.this, AtividadeControlar.class);
                    intent2.putExtra("vetorEnviarDadosControle", vetorEnviarDadosControle);
                    startActivity(intent2);
                }
            }
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