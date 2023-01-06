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

public class AtividadeMonitorar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    int qualBotaoClicado;
    int qualBotaoClicadoLooper;
    int putRemoverEModifier;

    int puloDoGato;
    // ------------------------------------------------------------------------------------------------------//
    public final static String SHARE_PREFS_MMONITORAR = "sharePrefsMonitorar";
    public final static String ARRAYLIST_MONITORAR = "ArrayListMonitorar";
    public final static String VERIFICADOR = "verificador";
       // ------------------------------------------------------------------------------------------------------//
    //String[] vetorPegarDados;
    //String[] vetorEnviarDados;
    //String[] vetorPegarDadosModificados;
    ArrayList<String[]> listaVetores = new ArrayList<>();
    ArrayList<Button> listaButton = new ArrayList<>();
    // ------------------------------------------------------------------------------------------------------//
    ImageView btnCriarMonitoramento, btnDeletarMonitoramento, btnModificarMonitoramento;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
    // ------------------------------------------------------------------------------------------------------//
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_monitorar);
        drawerLayout = findViewById(R.id.AtividadeMonitorar);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // ------------------------------------------------------------------------------------------------------//
        btnCriarMonitoramento = findViewById(R.id.btnCriarMonitoramento);
        btnDeletarMonitoramento = findViewById(R.id.btnDeletarMonitoramento);
        btnModificarMonitoramento = findViewById(R.id.btnModificarMonitoramento);
        btn1 = findViewById(R.id.btnMonitorar1);
        btn2 = findViewById(R.id.btnMonitorar2);
        btn3 = findViewById(R.id.btnMonitorar3);
        btn4 = findViewById(R.id.btnMonitorar4);
        btn5 = findViewById(R.id.btnMonitorar5);
        btn6 = findViewById(R.id.btnMonitorar6);
        // ------------------------------------------------------------------------------------------------------//
        listaButton.add(btn1);
        listaButton.add(btn2);
        listaButton.add(btn3);
        listaButton.add(btn4);
        listaButton.add(btn5);
        listaButton.add(btn6);
        listaButton.add(btn7);
        listaButton.add(btn8);
        // ------------------------------------------------------------------------------------------------------//
        loadPrefs();
        receberDadosSimples();
        receberDadosModificados();
        atribuirBtn();
        putRemoverEModifier = 0;
        puloDoGato = 0;
        // ------------------------------------------------------------------------------------------------------//
        btnDeletarMonitoramento.setOnClickListener(v -> {
            setBtnDeletarMonitoramento();
        });
        btnModificarMonitoramento.setOnClickListener(v -> {
            setbtnModificarMonitoramento();
        });
    }

    public void receberDadosSimples(){
        Intent intent = getIntent();
        String[] vetorPegarDados = intent.getStringArrayExtra("vetorResposta");
        if (vetorPegarDados != null) {
            listaVetores.add(vetorPegarDados);
            savePrefs();
        }
    }

    public void receberDadosModificados() {
        Intent intent1 = getIntent();
        String[] vetorPegarDadosModificados = intent1.getStringArrayExtra("vetorRespostaModificada");
        puloDoGato++;
        if (puloDoGato == 1){
            if (vetorPegarDadosModificados != null) {
                String[] vetorPegarDados = new String[7];
                for (int i = 0; i < ((vetorPegarDadosModificados.length) - 1); i++) {
                    vetorPegarDados[i] = vetorPegarDadosModificados[i];
                }
                int refButton = Integer.parseInt(vetorPegarDadosModificados[7]);
                listaVetores.set(refButton, vetorPegarDados);
                //listaVetores.remove(refButton);
                //listaVetores.add(refButton, vetorPegarDados);
                savePrefs();
            }
        }
    }

    public void adaptarLista(){

    }

    public void atribuirBtn(){
        for (int i = 0; i < listaVetores.size(); i++) {
            listaButton.get(i).setEnabled(false);
            listaButton.get(i).setVisibility(View.INVISIBLE);
            listaButton.get(i).setText("");
        }
        for (int i = 0; i < listaVetores.size(); i++){
            listaButton.get(i).setText(listaVetores.get(i)[0]);
            listaButton.get(i).setEnabled(true);
            listaButton.get(i).setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(), "BATUTINHA POGBOM", Toast.LENGTH_SHORT).show();
        }
        putRemoverEModifier = 0;
    }

    public void savePrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS_MMONITORAR, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaVetores);
        editor.putString(ARRAYLIST_MONITORAR, json);
        editor.apply();
    }

    public void loadPrefs(){
            SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS_MMONITORAR, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(ARRAYLIST_MONITORAR, null);
            Type type = new TypeToken<ArrayList<String[]>>() {}.getType();
            listaVetores = gson.fromJson(json, type);
            if (listaVetores == null) {
                listaVetores = new ArrayList<>();
            }
    }

    public void openCriarMonitorar(View view){
        if (listaVetores.size() < 8) {
            Intent intent4 = new Intent(getApplicationContext(), AtividadeCriarMonitoramento.class);
            startActivity(intent4);
        }
        else {
            Toast.makeText(getApplicationContext(), "Limite máximo de campos atingido", Toast.LENGTH_SHORT).show();
        }

    }
    public void openAcompanharMonitoramento(){
        switch (putRemoverEModifier){
            case 0:
                Intent intent5 = new Intent(getApplicationContext(), AtividadeAcompanharMonitoramento.class);
                String [] vetorEnviarDados1 = listaVetores.get(qualBotaoClicado);
                intent5.putExtra("vetorAcompanharMonitoramento", vetorEnviarDados1);
                startActivity(intent5);
                break;
            case 1:
                putRemoverEModifier = 0;
                listaVetores.remove(qualBotaoClicado);
                savePrefs();
                reload();
                atribuirBtn();
                break;
            case 2:
                putRemoverEModifier = 0;
                String [] vetorEnviarDados2 = new String[8];
                Intent intent6 = new Intent(getApplicationContext(), AtividadeCriarMonitoramento.class);
                for (int i = 0; i < listaVetores.get(qualBotaoClicado).length; i++) {
                    vetorEnviarDados2[i] = listaVetores.get(qualBotaoClicado)[i];
                }
                vetorEnviarDados2[7] = Integer.toString(qualBotaoClicado);
                intent6.putExtra("vetorModificarMonitoramento", vetorEnviarDados2);
                startActivity(intent6);
                break;
        }

    }

    public void colocarBtn1(View view) { qualBotaoClicado = 0; openAcompanharMonitoramento();}
    public void colocarBtn2(View view) { qualBotaoClicado = 1; openAcompanharMonitoramento();}
    public void colocarBtn3(View view) { qualBotaoClicado = 2; openAcompanharMonitoramento();}
    public void colocarBtn4(View view) { qualBotaoClicado = 3; openAcompanharMonitoramento();}
    public void colocarBtn5(View view) { qualBotaoClicado = 4; openAcompanharMonitoramento();}
    public void colocarBtn6(View view) { qualBotaoClicado = 5; openAcompanharMonitoramento();}
    public void colocarBtn7(View view) { qualBotaoClicado = 6; openAcompanharMonitoramento();}
    public void colocarBtn8(View view) { qualBotaoClicado = 7; openAcompanharMonitoramento();}





    public void reload() {
         Intent reloadIntent = new Intent(AtividadeMonitorar.this, AtividadeMonitorar.class);
         String[] a = null;
         String[] b = null;
         reloadIntent.putExtra("vetorModificarMonitoramento", a);
         reloadIntent.putExtra("vetorAcompanharMonitoramento", b);
         finish();
         startActivity(reloadIntent);
         //Toast.makeText(getApplicationContext(), "AAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT).show();
    }



    public void setBtnDeletarMonitoramento() {
        putRemoverEModifier = 1;
        Toast.makeText(getApplicationContext(), "ESCOLHAR O CAMPO PARA DELETAR", Toast.LENGTH_SHORT).show();
    }

    public void setbtnModificarMonitoramento(){
        putRemoverEModifier = 2;
        Toast.makeText(getApplicationContext(), "ESCOLHAR O CAMPO PARA MODIFICAR", Toast.LENGTH_SHORT).show();
    }

    public void setbtnCriarMonitoramento(){
        Intent intent4 = new Intent(getApplicationContext(), AtividadeAcompanharMonitoramento.class);
        String [] vetorEnviarDados = listaVetores.get(qualBotaoClicado);
        intent4.putExtra("vetorAcompanharMonitoramento", vetorEnviarDados);
        startActivity(intent4);

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
    public void deleteALL(){
        listaVetores.clear();
    }
    public void limpaTudoCarai(){
        for (int i = 0; i < listaButton.size(); i++){
            listaButton.get(i).setEnabled(false);
            listaButton.get(i).setVisibility(View.INVISIBLE);
            listaButton.get(i).setText("");
        }
    }
}