package com.example.daumhelpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
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
    public Button btnMeusPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        imvFotoUsuario = findViewById(R.id.imvFotoUsuario);
        txtNomeUser = findViewById(R.id.txtNomeUser);
        lstPedidos = findViewById(R.id.lstPedidos);
        btnMeusPedidos = findViewById(R.id.btnListarPedidos);

        Intent intentRecebedora = getIntent();

        parametros = intentRecebedora.getExtras();


        String email = parametros.getString("email_do_usuario");
        if (!email.equals("")){
            String NomeUser = helper.buscaNome(email);

            txtNomeUser.setText(NomeUser);

            Bitmap bm = helper.buscaFoto(email);
            if (bm != null) {
                imvFotoUsuario.setImageBitmap(bm);
            }
        }

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
        Cursor dados = helper.listarPedidosUserBD(email);

        if (dados != null) {

            String tabausuarios;
            final int vetor_id[];
            int i=0;
            vetor_id = new int[dados.getCount()];
            while (dados.moveToNext()) {
                tabausuarios = "ID: "
                        + dados.getString(0) + " | Título: "
                        + dados.getString(1) + " | Descrição: "
                        + dados.getString(2) + " | Pagamento:"
                        + dados.getString(3) + " | Tipo:"
                        + dados.getString(4);

                        vetor_id[i] = dados.getInt(0);
                        i = i + 1;
                lista.add(tabausuarios);


                final ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
                lstPedidos.setAdapter(listAdapter);
            }
                lstPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        PopupMenu popup = new PopupMenu(Configuracoes.this, view);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.poopup_menu, popup.getMenu());
                        popup.show();

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String i = (String) item.getTitle();
                                if (i.equals("Editar Pedido")){
                                    Intent intent = new Intent(Configuracoes.this, EditarPedido.class);
                                    Bundle parametros = new Bundle();
                                    int pos = position;
                                    parametros.putIntArray("vetor_id",vetor_id);
                                    parametros.putInt("pos",pos);
                                    //String pos = Integer.toString(position);
                                    intent.putExtras(parametros);
                                    startActivity(intent);
                                    //Toast.makeText(Configuracoes.this,pos,Toast.LENGTH_SHORT).show();
                                }else {
                                    //remover pedido


                                }


                                return true;
                            }
                        });

                        return true;
                    }
                });
        } else {
            String nop = "NENHUM PEDIDO CADASTRADO!";
            lista.add(nop);
            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            lstPedidos.setAdapter(listAdapter);
        }



    }

    public void meuspedidos(View view) {
        String email = parametros.getString("email_do_usuario");
        listarpedidosuser(email);
    }
}
