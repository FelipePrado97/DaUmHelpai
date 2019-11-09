package com.example.daumhelpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

public class Configuracoes extends AppCompatActivity {
    DataBaseHelper helper = new DataBaseHelper(this);

    public ImageView imvFotoUsuario;
    public TextView txtNomeUser;
    public Bundle parametros;
    public ListView lstPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        imvFotoUsuario = findViewById(R.id.imvFotoUsuario);
        txtNomeUser = findViewById(R.id.txtNomeUser);
        lstPedidos = findViewById(R.id.lstPedidos);


        Intent intentRecebedora = getIntent();
        parametros = intentRecebedora.getExtras();

        String email = parametros.getString("email_do_usuario");
        String NomeUser = helper.buscaNome(email);

        txtNomeUser.setText(NomeUser);

        Bitmap bm = helper.buscaFoto(email);
        if (bm != null) {
            imvFotoUsuario.setImageBitmap(bm);
        }

        listarpedidosuser(email);
    }


    public void abrirPrincipal(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(Configuracoes.this, Principal.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario", email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    public void editarPerfil(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(Configuracoes.this, EditarPerfil.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario", email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    public void configFiltro(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(Configuracoes.this, ConfigFiltro.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario", email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    public void abrirMensagens(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(Configuracoes.this, Mensagens.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario", email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    private void listarpedidosuser(String email) {
        ArrayList<String> lista = new ArrayList<>();
        Cursor dados = helper.listarPedidosUserBD();

        if (dados != null) {

            String tabausuarios;
            while (dados.moveToNext() && email.equals(dados.getString(5))) {
                tabausuarios = "ID: "
                        + dados.getString(0) + " | Título: "
                        + dados.getString(1) + " | Descrição: "
                        + dados.getString(2) + " | Pagamento:"
                        + dados.getString(3) + " | Tipo:"
                        + dados.getString(4);

                lista.add(tabausuarios);


                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
                lstPedidos.setAdapter(listAdapter);
            }
        } else {
            String nop = "NENHUM PEDIDO CADASTRADO!";
            lista.add(nop);
            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            lstPedidos.setAdapter(listAdapter);
        }

    }
}
