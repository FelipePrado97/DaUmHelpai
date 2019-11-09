package com.example.daumhelpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Mensagens extends AppCompatActivity {

    public ListView lstMensagens;
    DataBaseHelper helper = new DataBaseHelper(this);
    public Bundle parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);

        lstMensagens = findViewById(R.id.lstMensagens);

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();

        preencherlistview();



    }
        private void preencherlistview() {
        ArrayList<String> lista = new ArrayList<>();
        Cursor dados = helper.listarusuarios();

            if (dados.getCount()>0){

            String tabausuarios;
            while (dados.moveToNext()){
                tabausuarios = "IDº: "+dados.getString(0)+" | : "+dados.getString(1)+" | : "
                        +dados.getString(2)+" | "
                        +dados.getString(3);

                lista.add(tabausuarios);


                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,lista);
                lstMensagens.setAdapter(listAdapter);
            }
        }else {
            String nop = "Nenhum USUARIO CADASTRADO!";
            lista.add(nop);
            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,lista);
                lstMensagens.setAdapter(listAdapter);
        }
    }
}
