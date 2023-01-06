package com.Getai.IOTirrig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AtividadeControlar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    int putRemovereModifierControle;
    int btnClicar;
    // ------------------------------------------------------------------------------------------------------//
    public final static String SHARE_PREFS_CONTROLAR = "sharePrefsControalr";
    public final static String ARRAYLIST_CONTROLAR = "ArrayListControlar";
    // ------------------------------------------------------------------------------------------------------//
    ArrayList<String[]> listaVetoresCriar = new ArrayList<>();
    ArrayList<Button> listaButtonCriar = new ArrayList<>();
    // ------------------------------------------------------------------------------------------------------//
    ImageView btnCriarControle, btnDeletarControle, btnModificarControle;
    Button btn1Controlar, btn2Controlar, btn3Controlar, btn4Controlar, btn5Controlar, btn6Controlar, btn7Controlar, btn8Controlar;
    // ------------------------------------------------------------------------------------------------------//
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_controlar);
        drawerLayout = findViewById(R.id.atividadeControlar);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // ------------------------------------------------------------------------------------------------------//
        btnCriarControle = findViewById(R.id.btnCriarControle);
        btnDeletarControle = findViewById(R.id.btnDeletarControle);
        btnModificarControle = findViewById(R.id.btnModificarControle);
        btn1Controlar = findViewById(R.id.btnControlar1);
        btn2Controlar = findViewById(R.id.btnControlar2);
        btn3Controlar = findViewById(R.id.btnControlar3);
        btn4Controlar = findViewById(R.id.btnControlar4);
        btn5Controlar = findViewById(R.id.btnControlar5);
        btn6Controlar = findViewById(R.id.btnControlar6);
        // ------------------------------------------------------------------------------------------------------//
        listaButtonCriar.add(btn1Controlar);
        listaButtonCriar.add(btn2Controlar);
        listaButtonCriar.add(btn3Controlar);
        listaButtonCriar.add(btn4Controlar);
        listaButtonCriar.add(btn5Controlar);
        listaButtonCriar.add(btn6Controlar);
        listaButtonCriar.add(btn7Controlar);
        listaButtonCriar.add(btn8Controlar);
        // ------------------------------------------------------------------------------------------------------//
        btnCriarControle.setOnClickListener(v -> {
            openCriarControle();
        });
        btnModificarControle.setOnClickListener(v -> {
            openMdifcarControlar();
        });
        btnDeletarControle.setOnClickListener(v -> {
            openDeletarControlar();
        });

        loadPrefsControle();
        receberDadosCriarSimples();
        receberDadosModificadosControle();
        atribuirBtnControle();
        putRemovereModifierControle = 0;

    }

    public void receberDadosCriarSimples(){
        Intent intent = getIntent();
        String [] vetorReceberDadosSimples = intent.getStringArrayExtra("vetorEnviarDadosControle");
        if (vetorReceberDadosSimples != null){
            listaVetoresCriar.add(vetorReceberDadosSimples);
            savePrefsControle();
        }
    }

    public void receberDadosModificadosControle() {
        Intent intent1 = getIntent();
        String[] vetorPegarDadosModificados = intent1.getStringArrayExtra("vetorReceberDadosModificadosControle");
            if (vetorPegarDadosModificados != null) {
                String[] vetorPegarDados = new String[4];
                for (int i = 0; i < ((vetorPegarDadosModificados.length) - 1); i++) {
                    vetorPegarDados[i] = vetorPegarDadosModificados[i];
                }
                int refButton = Integer.parseInt(vetorPegarDadosModificados[4]);
                listaVetoresCriar.set(refButton, vetorPegarDados);
                savePrefsControle();
            }
    }

    public void savePrefsControle(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS_CONTROLAR, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaVetoresCriar);
        editor.putString(ARRAYLIST_CONTROLAR, json);
        editor.apply();
    }

    public void loadPrefsControle(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS_CONTROLAR, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(ARRAYLIST_CONTROLAR, null);
        Type type = new TypeToken<ArrayList<String[]>>() {}.getType();
        listaVetoresCriar = gson.fromJson(json, type);
        if (listaVetoresCriar == null) {
            listaVetoresCriar = new ArrayList<>();
        }
    }

    public void atribuirBtnControle(){
        for (int i = 0; i < listaVetoresCriar.size(); i++) {
            listaButtonCriar.get(i).setEnabled(false);
            listaButtonCriar.get(i).setVisibility(View.INVISIBLE);
            listaButtonCriar.get(i).setText("");
        }
        for (int i = 0; i < listaVetoresCriar.size(); i++){
            listaButtonCriar.get(i).setText(listaVetoresCriar.get(i)[0]);
            listaButtonCriar.get(i).setEnabled(true);
            listaButtonCriar.get(i).setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(), "BATUTINHA POGBOM", Toast.LENGTH_SHORT).show();
        }

    }

    public void setBtn1Controlar(View view) { btnClicar = 0; openAcompanharMonitoramento();}
    public void setBtn2Controlar(View view) { btnClicar = 1; openAcompanharMonitoramento();}
    public void setBtn3Controlar(View view) { btnClicar = 2; openAcompanharMonitoramento();}
    public void setBtn4Controlar(View view) { btnClicar = 3; openAcompanharMonitoramento();}
    public void setBtn5Controlar(View view) { btnClicar = 4; openAcompanharMonitoramento();}
    public void setBtn6Controlar(View view) { btnClicar = 5; openAcompanharMonitoramento();}
    public void setBtn7Controlar(View view) { btnClicar = 6; openAcompanharMonitoramento();}
    public void setBtn8Controlar(View view) { btnClicar = 7; openAcompanharMonitoramento();}





    private void openAcompanharMonitoramento() {
        switch (putRemovereModifierControle){
            case 0:
                Intent intent5 = new Intent(getApplicationContext(), AtividadeAcompanharControlar.class);
                String [] vetorEnviarDados1 = listaVetoresCriar.get(btnClicar);
                intent5.putExtra("vetorAcompanharCriar", vetorEnviarDados1);
                startActivity(intent5);
                break;
            case 1:
                putRemovereModifierControle = 0;
                listaVetoresCriar.remove(btnClicar);
                savePrefsControle();
                reloadControle();
                atribuirBtnControle();
                break;
            case 2:
                putRemovereModifierControle = 0;
                String [] vetorEnviarDados2 = new String[5];
                Intent intent6 = new Intent(getApplicationContext(), AtividadeCriarControle.class);
                for (int i = 0; i < listaVetoresCriar.get(btnClicar).length; i++) {
                    vetorEnviarDados2[i] = listaVetoresCriar.get(btnClicar)[i];
                }
                vetorEnviarDados2[4] = Integer.toString(btnClicar);
                intent6.putExtra("vetorModificarCriar", vetorEnviarDados2);
                startActivity(intent6);
                break;
        }
    }

    public void reloadControle() {
        Intent reloadIntent = new Intent(AtividadeControlar.this, AtividadeControlar.class);
        String[] a = null;
        String[] b = null;
        reloadIntent.putExtra("vetorEnviarDadosControle", a);
        reloadIntent.putExtra("vetorReceberDadosModificadosControle", b);
        finish();
        startActivity(reloadIntent);
        //Toast.makeText(getApplicationContext(), "AAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT).show();
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
    public void openCriarControle(){
        if (listaVetoresCriar.size() < 8) {
            Intent intent4 = new Intent(getApplicationContext(), AtividadeCriarControle.class);
            startActivity(intent4);
        }
        else {
            Toast.makeText(getApplicationContext(), "Limite máximo de campos atingido", Toast.LENGTH_SHORT).show();
        }

    }
    public void openMdifcarControlar(){
        putRemovereModifierControle = 2;
    }
    public void openDeletarControlar(){
       putRemovereModifierControle = 1;
    }
}