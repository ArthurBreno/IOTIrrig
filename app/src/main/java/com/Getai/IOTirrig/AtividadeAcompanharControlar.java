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
import android.widget.Button;
import android.widget.ImageView;
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

public class AtividadeAcompanharControlar extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener  {

    Handler handlerUnicoLigar = new Handler();
    // ------------------------------------------------------------------------------------------------------//
    String estado;
    int contador = 0;
    // ------------------------------------------------------------------------------------------------------//
    String qualSegundaParte = "";
    String respondendo;
    String respondendoDesligar;
    String respondendoLigar;
    String campoParaleitura;
    String urlLeitur;
    String urlEscrita;
    String modoEnviado;
    // ------------------------------------------------------------------------------------------------------//
    TextView tvTituloControlar, tvStatusBomba;
    Button btnLigar, btnDesligar;
    TextView tvTituloBarraInferior;

    ImageView imageLoop, imageRelogio;
    // ------------------------------------------------------------------------------------------------------//
    //RequestQueue queueLigarBomba, queueDefBombaStatus;
    Handler handlerEsperarResposta = new Handler();
    Handler handlerRunnableStatusBomba = new Handler();
    // ------------------------------------------------------------------------------------------------------//
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_acompanhar_controlar);
        drawerLayout = findViewById(R.id.atividadeAcompanharControlar);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // ------------------------------------------------------------------------------------------------------//
        tvTituloControlar = findViewById(R.id.tvTituloControlar);
        tvStatusBomba = findViewById(R.id.tvBombStatus);
        btnLigar = findViewById(R.id.btnLigado);
        btnDesligar = findViewById(R.id.btnDesligado);
        // ------------------------------------------------------------------------------------------------------//
        tvTituloBarraInferior = findViewById(R.id.tvTituloBarraInferior);
        imageLoop = findViewById(R.id.imageLoop);
        imageRelogio = findViewById(R.id.imageRelogio);
        // ------------------------------------------------------------------------------------------------------//
        imageRelogio.setOnClickListener(v -> {
            qualSegundaParte = "relogio";
            enviarParaEspecial();

        });
        imageLoop.setOnClickListener(v -> {
            qualSegundaParte = "loop";
            enviarParaEspecial();

        });
        btnLigar.setOnClickListener(v -> {
            modoEnviado = "ligado";
            ligarBomba();
        });
        btnDesligar.setOnClickListener(v -> {
            modoEnviado = "desligado";
            desligarBomba();
        });
        receberVetor();
        waitResponse.run();
    }
    public void receberVetor(){
        Intent intent = getIntent();
        String[] vetorReceberDados = intent.getStringArrayExtra("vetorAcompanharCriar");
        tvTituloControlar.setText(vetorReceberDados[0]);
        urlEscrita = vetorReceberDados[1];
        urlLeitur = vetorReceberDados[2];
        campoParaleitura = vetorReceberDados[3];
    }


    public void ligarBomba(){
        String respostaServidor;
        String urlCorrigido = urlEscrita.substring(0, urlEscrita.length()-3) + campoParaleitura + "=1";
        // String urlCorrigido = "https://api.thingspeak.com/update?api_key=1GPHG6IY3PE3694O&field1=1";
        StringRequest request = new StringRequest(Request.Method.POST,  urlCorrigido, response -> {
            respondendoLigar = response;
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queueLigarBomba = Volley.newRequestQueue(this);
        queueLigarBomba.add(request);
        btnLigar.setBackgroundResource(R.drawable.btn_verde_escuro);
        btnDesligar.setBackgroundResource(R.drawable.btn_vermelho_claro);
        //tvStatusBomba.setText(respondendoLigar);
        estado = "LIGANDO...";
        runnableStatusBomba.run();
        //return respondendoLigar;
    }

    public void desligarBomba(){
        String urlCorrigido = urlEscrita.substring(0, urlEscrita.length()-3) + campoParaleitura + "=0";
        //  String urlCorrigido = "https://api.thingspeak.com/update?api_key=1GPHG6IY3PE3694O&field1=0";
        StringRequest request  = new StringRequest(Request.Method.POST, urlCorrigido, response -> {
            respondendoLigar = response;
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //tvStatusBomba.setText(respondendo);
        RequestQueue queue2 = Volley.newRequestQueue(this);
        queue2.add(request);
        //Toast.makeText(getApplicationContext(), "Leitura automatica está ligada", Toast.LENGTH_SHORT).show();
        btnDesligar.setBackgroundResource(R.drawable.btn_vermelho_escuro);
        btnLigar.setBackgroundResource(R.drawable.btn_verde_claro);
        //tvStatusBomba.setText(respondendoLigar);
        estado = "DESLIGANDO...";
        runnableStatusBomba.run();
        //return respondendoLigar;
    }

    public void defStatusBomba(){
         String urlCorrigido = urlLeitur.substring(0, urlLeitur.length()-1);
        //  String urlPegarDado = "https://api.thingspeak.com/channels/1300743/feeds.json?results=1";
        JsonObjectRequest requestDefStatusBomba = new JsonObjectRequest(Request.Method.GET, urlCorrigido, null, response -> {
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
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //tvStatusBomba.setText(respondendo);
        RequestQueue queueDefBombaStatus = Volley.newRequestQueue(this);
        queueDefBombaStatus.add(requestDefStatusBomba);
       // return respondendo;
    }

    public final Runnable runnableStatusBomba = new Runnable() {
        @Override
        public void run() {
            defStatusBomba();
            //handlerRunnableStatusBomba = new Handler();
            //Toast.makeText(getApplicationContext(), respondendoLigar, Toast.LENGTH_SHORT).show();
            if (respondendoLigar != null &&  !(respondendoLigar.equals("0"))) {
                if (respondendo != null) {
                    if (respondendo.equals("1") && modoEnviado.equals("ligado")) {
                        defStatusBomba();
                        tvStatusBomba.setText("ON");
                        handlerRunnableStatusBomba.removeCallbacks(runnableStatusBomba);

                    }
                        //respondendo = null;
                    if (respondendo.equals("0") && modoEnviado.equals("desligado")) {
                        defStatusBomba();
                        tvStatusBomba.setText("OFF");
                        handlerRunnableStatusBomba.removeCallbacks(runnableStatusBomba);
                        //respondendo = null;
                    }
                   // Toast.makeText(getApplicationContext(), "verificando valor", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                handlerRunnableStatusBomba.postDelayed(runnableStatusBomba, 5000);
            }
        }
    };

//    private final Runnable runnableStatusBomba = new Runnable() {
  //      @Override
  //      public void run() {
   //         if (estado == "ligada"){
    //            if (respondendo == "0"){
    //                tvStatusBomba.setText("ON");
    //            }
    //        }
    //        if (estado == "desligada"){
    //            if (respondendo == "0"){
    //                tvStatusBomba.setText("OFF");
     //           }
      //      }


            //    if (respondendo.equals("0")) {
                    //handlerDefStatusBomba.removeCallbacks(runnableStatusBomba);
           //         tvStatusBomba.setText("DESLIGADA");
           //         Toast.makeText(getApplicationContext(), "lop paro", Toast.LENGTH_SHORT).show();
           //     }
           //     if (respondendo.equals("1")) {
            //        //handlerDefStatusBomba.removeCallbacks(runnableStatusBomba);
            //        tvStatusBomba.setText("LIGADA");
            //        Toast.makeText(getApplicationContext(), "lop  paro", Toast.LENGTH_SHORT).show();

         //   }else {
         //       handlerDefStatusBomba.postDelayed(runnableStatusBomba, 2000);
         //       Toast.makeText(getApplicationContext(), "lop continua", Toast.LENGTH_SHORT).show();
         //   }
         //   Toast.makeText(getApplicationContext(), "lop continua22222", Toast.LENGTH_SHORT).show();
            //  handlerDefStatusBomba.postDelayed(runnableStatusBomba, 2000);
            // rodador.run();
      //  }
   // };

private final Runnable waitResponse = new Runnable() {
        @Override
        public void run() {
            defStatusBomba();
            contador++;
                if (respondendo != null && respondendo.equals("0")) {
                   // Toast.makeText(getApplicationContext(), "DESLIGAR DESLIGAR", Toast.LENGTH_SHORT).show();
                    tvStatusBomba.setText("OFF");
                }
                if (respondendo != null &&  respondendo.equals("1")){
                    //Toast.makeText(getApplicationContext(), "LIGAR LIGAR", Toast.LENGTH_SHORT).show();
                    tvStatusBomba.setText("ON");
                }

                if(respondendo != null && respondendo.contains("a")){
                    //Toast.makeText(getApplicationContext(), "relogio relogio", Toast.LENGTH_SHORT).show();
                    tvStatusBomba.setText("TIMER ON");
                }

                if (respondendo != null && respondendo.contains("b")){
                    //Toast.makeText(getApplicationContext(), "loop loop", Toast.LENGTH_SHORT).show();
                    tvStatusBomba.setText("loop ON");
                }

            handlerEsperarResposta.postDelayed(waitResponse, 5000);
                // colocar aqui pra verificar se tem "a" ou "b" no respondendo//
        }
    };


    public void enviarParaEspecial(){
        Intent intent = new Intent(AtividadeAcompanharControlar.this, AtividadeAcompanharControlarEspecial.class);
        String[] vetorParaEspecial = new String[5];
        if (qualSegundaParte.equals("relogio")){
            vetorParaEspecial[0] = Integer.toString(0);
        }
        if (qualSegundaParte.equals("loop")){
            vetorParaEspecial[0] = Integer.toString(1);
        }
        String urlCorrigidoEscrita = urlEscrita.substring(0, urlEscrita.length()-3) + campoParaleitura + "=";
        vetorParaEspecial[1] = urlCorrigidoEscrita;
        String urlCorrigidoLeitura = urlLeitur.substring(0, urlLeitur.length()-1);
        vetorParaEspecial[2] = urlCorrigidoLeitura;
        vetorParaEspecial[3] = campoParaleitura;
        vetorParaEspecial[4] = tvTituloControlar.getText().toString();
        intent.putExtra("vatorEspecial", vetorParaEspecial);
        startActivity(intent);
        //finish();
    }


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
            case R.id.nav_inf4:
                Intent intent4 = new Intent(getApplicationContext(),AtividadeSobre.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onStop(){
        super.onStop();
        //finish();
        //Toast.makeText(AtividadeAcompanharControlar.this, "Atividade pausada", Toast.LENGTH_SHORT).show();
        handlerRunnableStatusBomba.removeCallbacks(runnableStatusBomba);
        handlerEsperarResposta.removeCallbacks(waitResponse);
    }

    @Override
    protected void onRestart() {
        super.onRestart(); // ultima modificação feita aqui
        waitResponse.run();
    }
}