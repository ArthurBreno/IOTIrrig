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

public class AtividadeCriarMonitoramento extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    boolean checkModificado = false;
    int posicao;
    // ------------------------------------------------------------------------------------------------------//
    public String[] vetorEnviar = new String[7];
    public String[] vetorModificar = new String[8];
    public ArrayList<EditText> listaUnidades = new ArrayList<>();
    // ------------------------------------------------------------------------------------------------------//
    EditText edTitulo, edUrl, edUnidade1, edUnidade2, edUnidade3, edUnidade4;
    Button btnSalvarMonitoramento;
    Spinner spnNumero;
    // ------------------------------------------------------------------------------------------------------//
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_criar_monitoramento);
        drawerLayout = findViewById(R.id.atividadeCiarMonitoramento);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // ------------------------------------------------------------------------------------------------------//
        edTitulo = findViewById(R.id.edTituloMonitoramento);
        edUrl =  findViewById(R.id.edUrlMonitoramento);
        spnNumero = findViewById(R.id.spnNumero);
        edUnidade1 = findViewById(R.id.edUnidade1);
        edUnidade2 = findViewById(R.id.edUnidade2);
        edUnidade3 = findViewById(R.id.edUnidade3);
        edUnidade4 = findViewById(R.id.edUnidade4);
        btnSalvarMonitoramento = findViewById(R.id.btnSalvarCriarMonitoramento);
        // ------------------------------------------------------------------------------------------------------//
        listaUnidades.add(edUnidade1);
        listaUnidades.add(edUnidade2);
        listaUnidades.add(edUnidade3);
        listaUnidades.add(edUnidade4);
        // ------------------------------------------------------------------------------------------------------//
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.numeros, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNumero.setAdapter(arrayAdapter);
        spnNumero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicao = position+1;
                for (int i = 0; i < posicao; i++) {
                    listaUnidades.get(i).setEnabled(true);
                    listaUnidades.get(i).setVisibility(View.VISIBLE);
                }
                for (int i = posicao; i < listaUnidades.size(); i++) {
                    listaUnidades.get(i).setEnabled(false);
                    listaUnidades.get(i).setVisibility(View.INVISIBLE);
                    listaUnidades.get(i).setText("");
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // ------------------------------------------------------------------------------------------------------//
        btnSalvarMonitoramento.setOnClickListener(v -> {
            verificarEdVasio();
            enviarDadosEfecharAtividade();
        });
        // ------------------------------------------------------------------------------------------------------//
        receberVetorParaModificar();
    }

    public boolean verificarEdVasio() {
        boolean resposta = false;
        int verificador = 6;
        for (int i = 0; i < listaUnidades.size(); i++){
            if (listaUnidades.get(i).isEnabled()){
               if (listaUnidades.get(i).getText().toString().isEmpty()){
                   verificador = 4;
               }
            }
        }
        if (edUrl.getText().toString().isEmpty()) {
            verificador = 3;
        }
        if (edTitulo.getText().toString().isEmpty()) {
            verificador = 2;
        }
        switch (verificador){
            case 2:
                Toast.makeText(getApplicationContext(), "PREECHER TITULO", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "PREECHER URL", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getApplicationContext(), "PREECHER Undiade de medída", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                resposta = true;
                break;
        }
        return resposta;
    }

    public void enviarDadosEfecharAtividade(){
       if(verificarEdVasio()) {
           if(vetorModificar != null){
               vetorModificar[0] = edTitulo.getText().toString();
               vetorModificar[1] = edUrl.getText().toString();
               vetorModificar[2] = Integer.toString(posicao);
               for (int i = 0; i < listaUnidades.size()-1; i++) {
                   vetorModificar[i + 3] = listaUnidades.get(i).getText().toString();
               }
               Intent intent = new Intent(getApplicationContext(), AtividadeMonitorar.class);
               intent.putExtra("vetorRespostaModificada", vetorModificar);
               startActivity(intent);
           }else {
               vetorEnviar[0] = edTitulo.getText().toString();
               vetorEnviar[1] = edUrl.getText().toString();
               vetorEnviar[2] = Integer.toString(posicao);
               for (int i = 0; i < listaUnidades.size(); i++) {
                   vetorEnviar[i + 3] = listaUnidades.get(i).getText().toString();
               }
               Intent intent = new Intent(getApplicationContext(), AtividadeMonitorar.class);
               intent.putExtra("vetorResposta", vetorEnviar);
               startActivity(intent);
           }
        }
       else {
           Toast.makeText(getApplicationContext(), "OCORREU UM ERRO POR FAVOR TENTE NOVAMENTE", Toast.LENGTH_SHORT).show();
       }
    }

    public void receberVetorParaModificar(){
        Intent intent = getIntent();
        vetorModificar = intent.getStringArrayExtra("vetorModificarMonitoramento");
        if (vetorModificar != null) {
            edTitulo.setText(vetorModificar[0]);
            edUrl.setText(vetorModificar[1]);
            int mostradorDeEd = Integer.parseInt(vetorModificar[2]);
            mostradorDeEd = mostradorDeEd - 1;
            //spnNumero.setSelection(2);
            spnNumero.setSelection(mostradorDeEd);
            for (int i = 0; i < Integer.parseInt(vetorModificar[2]); i++) {
                listaUnidades.get(i).setText(vetorModificar[i + 3]);
            }
            checkModificado = true;
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