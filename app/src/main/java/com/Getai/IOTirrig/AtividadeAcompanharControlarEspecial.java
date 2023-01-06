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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AtividadeAcompanharControlarEspecial extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    LinearLayout layInferior;
    // ------------------------------------------------------------------------------------------------------//
    String[] vettorRecebido;
    // ------------------------------------------------------------------------------------------------------//
    Handler handlerRelogio = new Handler();
    Handler handlerLoop = new Handler();
    Handler handlerVerificarEnvio = new Handler();
    // ------------------------------------------------------------------------------------------------------//
    String urlEscrita, urlLeitura, modo;
    String modoRelogio, modoLoop;
    String campoParaleitura;
    String titulo;
    String respondendo;
    String respostaParaEnvio;
    // ------------------------------------------------------------------------------------------------------//
    EditText edMinutoSuperior, edMinutoInferior, edSegundoSuperior, edSegundoInferior, edHoraSuperior, edHoraInferior;
    TextView tvSuperior, tvInferior, tvTitulo;
    Button btnInicarFechar;
    // ------------------------------------------------------------------------------------------------------//
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_acompanhar_controlar_especial);
        drawerLayout = findViewById(R.id.atividadeAcompanharControlarEspecial);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // ------------------------------------------------------------------------------------------------------//
        btnInicarFechar = findViewById(R.id.btnIniciarFechar);
        tvSuperior = findViewById(R.id.tvSuperior);
        tvInferior = findViewById(R.id.tvInferior);
        tvTitulo = findViewById(R.id.tvTituloBarraInferior);
        edSegundoSuperior = findViewById(R.id.edSegundoSuperior);
        edSegundoInferior = findViewById(R.id.edSegundoInferior);
        edMinutoSuperior = findViewById(R.id.edMinutoSuperior);
        edMinutoInferior = findViewById(R.id.edMinutoInferior);
        edHoraSuperior = findViewById(R.id.edHoraSuperior);
        edHoraInferior = findViewById(R.id.edHoraInferior);
        layInferior = findViewById(R.id.layLinearInferior);
        // ------------------------------------------------------------------------------------------------------//
        receberVetor();
        mostraIcones();
        btnInicarFechar.setOnClickListener(v -> {if(verifPreenchimentoDosCampos()){verificarEnvio.run();}});
    }

    public void receberVetor(){
        Intent intent = getIntent();
        vettorRecebido = intent.getStringArrayExtra("vatorEspecial");
        urlEscrita = vettorRecebido[1];
        urlLeitura = vettorRecebido[2];
        modo = vettorRecebido[0];
        campoParaleitura = vettorRecebido[3];
        titulo = vettorRecebido[4];

    }

    public void mostraIcones(){
        tvSuperior.setVisibility(View.VISIBLE);
        edSegundoSuperior.setVisibility(View.VISIBLE);
        edHoraSuperior.setVisibility(View.VISIBLE);
        edMinutoSuperior.setVisibility(View.VISIBLE);
        if (modo.equals("0")){
            tvTitulo.setText("TIMER");
            tvSuperior.setText("DEFINIR TEMPO DE FUNCIONAMENTO");
        }

        if (modo.equals("1")){
            tvTitulo.setText("LOOP");
            tvSuperior.setText("TEMPO LIGADO");
            tvInferior.setVisibility(View.VISIBLE);
            layInferior.setVisibility(View.VISIBLE);


        }
    }

    public void enviarParaServidor(){
        if (modo.equals("0")){
            int minitoSuperior = Integer.parseInt(edMinutoSuperior.getText().toString());
            int segundoSuperior = Integer.parseInt(edSegundoSuperior.getText().toString());
            int horaSuperior = Integer.parseInt(edHoraSuperior.getText().toString());
            horaSuperior = horaSuperior * 3600;
            minitoSuperior = minitoSuperior * 60;
            minitoSuperior =  horaSuperior + minitoSuperior + segundoSuperior;
            modoRelogio = "a".concat(Integer.toString(minitoSuperior));
            String urlCorrigido = urlEscrita + "a" + (minitoSuperior);
            StringRequest request = new StringRequest(Request.Method.POST, urlCorrigido, response -> {
                respostaParaEnvio = response;
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //error.printStackTrace();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue rq;
            rq = Volley.newRequestQueue(this);
            rq.add(request);
        }

        if (modo.equals("1")){
            int minitoSuperior = Integer.parseInt(edMinutoSuperior.getText().toString());
            int segundoSuperior = Integer.parseInt(edSegundoSuperior.getText().toString());
            int horaSuperior = Integer.parseInt(edHoraSuperior.getText().toString());
            int minutoInferior = Integer.parseInt(edMinutoInferior.getText().toString());
            int segundoInferior = Integer.parseInt(edSegundoInferior.getText().toString());
            int horaInferior = Integer.parseInt(edHoraInferior.getText().toString());
            horaSuperior = horaSuperior * 3600;
            minitoSuperior = minitoSuperior * 60;
            minitoSuperior = horaSuperior + minitoSuperior + segundoSuperior;
            horaInferior = horaInferior * 3600;
            minutoInferior = minutoInferior * 60;
            minutoInferior =  horaInferior + minutoInferior + segundoInferior;
            modoLoop = "b".concat(Integer.toString(minitoSuperior)).concat("c").concat(Integer.toString(minutoInferior));
            String urlCorrigido = urlEscrita + "b" + (minitoSuperior) + "c" + minutoInferior;
            StringRequest request = new StringRequest(Request.Method.POST, urlCorrigido, response -> {
                respostaParaEnvio = response;
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //error.printStackTrace();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue rq;
            rq = Volley.newRequestQueue(this);
            rq.add(request);
        }
    }


    public void verificarModoRelogio(){
        JsonObjectRequest requestDefStatusBomba = new JsonObjectRequest(Request.Method.GET, urlLeitura, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("feeds");
                int ii = 0;
                for (int i = jsonArray.length()-1;  ii < i; i--){
                    JSONObject feeds = jsonArray.getJSONObject(i);
                    if(!feeds.getString("field".concat(campoParaleitura)).equals("null")){
                        respondendo = feeds.getString("field".concat(campoParaleitura));
                        break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queueDefBombaStatus = Volley.newRequestQueue(this);
        queueDefBombaStatus.add(requestDefStatusBomba);
    }

    public boolean verifPreenchimentoDosCampos(){
        boolean verifcarPreenchimento = false;
        int checkPreenchimentoRelogio = 0;
        int checkPreenchimentoLoop = 0;
        if (modo.equals("0")){
            if (edHoraSuperior.getText().toString().isEmpty()){
                checkPreenchimentoRelogio = 1;
            }
            if (edMinutoSuperior.getText().toString().isEmpty()){
                checkPreenchimentoRelogio = 2;
            }
            if (edSegundoSuperior.getText().toString().isEmpty()){
                checkPreenchimentoRelogio = 3;
            }
            switch (checkPreenchimentoRelogio){
                case 0:
                    verifcarPreenchimento = true;
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Horas", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Minutos", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Segundos", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        if (modo.equals("1")){
            if (edHoraSuperior.getText().toString().isEmpty()){
                checkPreenchimentoLoop = 1;
            }
            if (edMinutoSuperior.getText().toString().isEmpty()){
                checkPreenchimentoLoop = 2;
            }
            if (edSegundoSuperior.getText().toString().isEmpty()){
                checkPreenchimentoLoop = 3;
            }
            if (edHoraInferior.getText().toString().isEmpty()){
                checkPreenchimentoLoop = 4;
            }
            if (edMinutoInferior.getText().toString().isEmpty()){
                checkPreenchimentoLoop = 5;
            }
            if (edSegundoInferior.getText().toString().isEmpty()){
                checkPreenchimentoLoop = 6;
            }
            switch (checkPreenchimentoLoop){
                case 0:
                    verifcarPreenchimento = true;
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Tempo Ligado Horas", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Tempo Ligado Minutos", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Tempo Ligado Segundos", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Tempo Desligado Horas", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Tempo Desligado Minutos", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "Preencher Campo: Tempo Desligado Segundos", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return verifcarPreenchimento;
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
    // ------------------------------------------------------------------------------------------------------//

    public final Runnable verificarRelogioRun = new Runnable() {
        @Override
        public void run() {
            verificarModoRelogio();
            if (respondendo != null && respondendo.equals(modoRelogio)){
                Toast.makeText(getApplicationContext(), "envio confirmado", Toast.LENGTH_SHORT).show();
                handlerRelogio.removeCallbacks(verificarRelogioRun);
                Intent intent = new Intent(AtividadeAcompanharControlarEspecial.this, AtividadeAcompanharControlar.class);
                String[] vetorRetorno = new String[4];
                vetorRetorno[0] = titulo;
                vetorRetorno[1] = urlEscrita.concat("0");
                vetorRetorno[2] = urlLeitura;
                vetorRetorno[3] = campoParaleitura;
                intent.putExtra("vetorAcompanharCriar", vetorRetorno);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "checando Envio", Toast.LENGTH_SHORT).show();
                enviarParaServidor();
                handlerRelogio.postDelayed(verificarRelogioRun, 4000);

            }
        }
    };

    public final Runnable verificaLoopRun = new Runnable() {
        @Override
        public void run() {
            verificarModoRelogio();
            if (respondendo != null && respondendo.equals(modoLoop)){
                handlerLoop.removeCallbacks(verificaLoopRun);
                Intent intent = new Intent(AtividadeAcompanharControlarEspecial.this, AtividadeAcompanharControlar.class);
                String[] vetorRetorno = new String[4];
                vetorRetorno[0] = titulo;
                vetorRetorno[1] = urlEscrita.concat("0");
                vetorRetorno[2] = urlLeitura;
                vetorRetorno[3] = campoParaleitura;
                intent.putExtra("vetorAcompanharCriar", vetorRetorno);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "checando Envio", Toast.LENGTH_SHORT).show();
                enviarParaServidor();
                handlerLoop.postDelayed(verificaLoopRun, 4000);
            }
        }
    };

    public final Runnable verificarEnvio = new Runnable() {
        @Override
        public void run() {
            enviarParaServidor();
            if (respostaParaEnvio != null && !(respostaParaEnvio.equals("0"))){
                handlerVerificarEnvio.removeCallbacks(verificarEnvio);
                if (modo.equals("0")){
                    verificarRelogioRun.run();
                }
                if (modo.equals("1")){
                    verificaLoopRun.run();
                }
            }
            else {
                handlerVerificarEnvio.postDelayed(verificarEnvio, 3000);
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        handlerRelogio.removeCallbacks(verificarRelogioRun);
        handlerLoop.removeCallbacks(verificaLoopRun);
        handlerVerificarEnvio.removeCallbacks(verificarEnvio);
    }
}