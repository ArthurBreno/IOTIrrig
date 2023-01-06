package com.Getai.IOTirrig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AtividadeAcompanharMonitoramento extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int contadorDeErros = 0;
    //-----------------------------------------------------------------------------------------------------------//
    ArrayList<TextView> listValores = new ArrayList<>();
    ArrayList<TextView> listunidades = new ArrayList<>();
    ArrayList<TextView> listaTvCompleta = new ArrayList<>();
    String[] vetorReceber = new String[7];
    String[] vetorFiels;
    //-----------------------------------------------------------------------------------------------------------//
    Handler handler = new Handler();
    String urlLeitura; //https://api.thingspeak.com/channels/1309371/feeds.json?api_key=6LR59FTIJ5KVZA2G&results=2
    //-----------------------------------------------------------------------------------------------------------//
    TextView tvNumero1, tvNumero2, tvNumero3, tvNumero4;
    TextView tvUnidade1, tvUnidade2, tvUnidade3, tvUnidade4;
    TextView tvTituloMonitoramento;
    //-----------------------------------------------------------------------------------------------------------//
    DrawerLayout drawerLayout;// n mexer nesse intervalor
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_acompanhar_monitoramento);
        drawerLayout = findViewById(R.id.atividadeAcompanharMonitoramento);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // ------------------------------------------------------------------------------------------------------//
        tvNumero1 = findViewById(R.id.tvNumero1);
        tvNumero2 = findViewById(R.id.tvNumero2);
        tvNumero3 = findViewById(R.id.tvNumero3);
        tvNumero4 = findViewById(R.id.tvNumero4);
        tvUnidade1 = findViewById(R.id.tvUnidade1);
        tvUnidade2 = findViewById(R.id.tvUnidade2);
        tvUnidade3 = findViewById(R.id.tvUnidade3);
        tvUnidade4 = findViewById(R.id.tvUnidade4);
        tvTituloMonitoramento = findViewById(R.id.tvTituloMonitoramento);
        // ------------------------------------------------------------------------------------------------------//
        listaTvCompleta.add(tvNumero1);
        listaTvCompleta.add(tvUnidade1);
        listaTvCompleta.add(tvNumero2);
        listaTvCompleta.add(tvUnidade2);
        listaTvCompleta.add(tvNumero3);
        listaTvCompleta.add(tvUnidade3);
        listaTvCompleta.add(tvNumero4);
        listaTvCompleta.add(tvUnidade4);
        listunidades.add(tvUnidade1);
        listunidades.add(tvUnidade2);
        listunidades.add(tvUnidade3);
        listunidades.add(tvUnidade4);
        listValores.add(tvNumero1);
        listValores.add(tvNumero2);
        listValores.add(tvNumero3);
        listValores.add(tvNumero4);
        // ------------------------------------------------------------------------------------------------------//
        receberVetor();
        tvTituloMonitoramento.setText(vetorReceber[0]);
        urlLeitura = vetorReceber[1];
        spawnTv();
        atribuirUnidade();
        criarVetorFildes();
        // ------------------------------------------------------------------------------------------------------//
        runnableLeitura.run();
    }

    public void receberVetor(){
        Intent intent = getIntent();
        vetorReceber = intent.getStringArrayExtra("vetorAcompanharMonitoramento");
    }

    public void spawnTv(){
        int quatidade = (Integer.parseInt(vetorReceber[2]) ) * 2;
        for (int i = 0; i < quatidade; i++ ){
            listaTvCompleta.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void atribuirUnidade(){
        int quatidade = Integer.parseInt(vetorReceber[2]); // 1 2 3 4 /////  3 - 4 - 5 - 6
        for (int i = 0; i < quatidade ; i++) {
            listunidades.get(i).setText(vetorReceber[i + 3]);
        }
    }

    public void verificarIntegridadeValores(){
        for (int i = 0; i < vetorFiels.length; i++) {
            if (vetorFiels[i] != null  && !vetorFiels[i].equals("null")){
                listValores.get(i).setText(vetorFiels[i]);
            }
        }
    }

    public void criarVetorFildes(){
        vetorFiels = new String[Integer.parseInt(vetorReceber[2])];
    }

    private final Runnable runnableLeitura = new Runnable() {
        @Override
        public void run() {
            //Toast.makeText(getApplicationContext(), "testando 1234657891234589", Toast.LENGTH_SHORT).show();
            String urlCorrigido = urlLeitura.substring(0, urlLeitura.length()-1);//update remoção do "4"
            JsonObjectRequest requestDefStatusBomba = new JsonObjectRequest(Request.Method.GET, urlCorrigido, null, response -> {
                try {
                    JSONArray jsonArray = response.getJSONArray("feeds");
                    JSONObject feeds = jsonArray.getJSONObject(jsonArray.length()-1);
                    for (int i = 0; i < Integer.parseInt(vetorReceber[2]); i++) {
                        String campo = "field".concat(Integer.toString(i+1));
                        vetorFiels[i] = feeds.getString(campo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //error.printStackTrace();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    contadorDeErros++;
                }
            });
            RequestQueue queueDefBombaStatus = Volley.newRequestQueue(getApplicationContext());
            queueDefBombaStatus.add(requestDefStatusBomba);
            verificarIntegridadeValores();
            handler.postDelayed(runnableLeitura, 2000);

            if (contadorDeErros >= 5){
                Toast.makeText(getApplicationContext(), "OCORREU UM PROBLEMA DURANTE A REQUISIÇÃO DE DADOS", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(runnableLeitura);
                finish();
            }

        }
    };

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

    @Override
    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(runnableLeitura);
        //Toast.makeText(getApplicationContext(), "atividade pausada", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStop(){
        super.onStop();
        handler.removeCallbacks(runnableLeitura);
        //Toast.makeText(getApplicationContext(), "atividade pausada", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnableLeitura);
        runnableLeitura.run();
        //Toast.makeText(getApplicationContext(), "ativdade resumida", Toast.LENGTH_SHORT).show();

    }
}