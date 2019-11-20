package com.example.daumhelpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Mensagens extends AppCompatActivity {

    public ListView lstMensagens, lstPedidos;
    DataBaseHelper helper = new DataBaseHelper(this);
    public Bundle parametros;
    public String celulares[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);

        lstPedidos = findViewById(R.id.lstPedidos);

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();

        String email = parametros.getString("email_do_usuario");
        ArrayList<String> lista = new ArrayList<>();
        int id_usuario = helper.buscaIdUsuario(email);
        Cursor cursor = helper.listarpedidosAceitos(id_usuario);
        //id dos pedidos aceitos
        if (cursor!=null){
            while (cursor.moveToNext()){
                //busca email da pessoa que criou o pedido
                //depois mostra o nome, o nome do pedido e busca o celular
                int id_pedido_aceito = cursor.getInt(0);
                Cursor dados_pedido_aceito =  helper.buscaDadosdoPedido(id_pedido_aceito);//busca dados dos pedidos aceitos
                if (dados_pedido_aceito!=null){
                    String dados, nome,email_criador_pedido;

                    celulares = new String[cursor.getCount()];
                    int i = 0;
                    while (dados_pedido_aceito.moveToNext()){
                        email_criador_pedido = dados_pedido_aceito.getString(0);
                        celulares[i] = helper.buscarCelular(email_criador_pedido);
                        nome = helper.buscaNome(dados_pedido_aceito.getString(0));
                        dados = "Nome:" +nome+ " Titulo: "+ dados_pedido_aceito.getString(1)+" Pagamento: "+dados_pedido_aceito.getString(2);
                        lista.add(dados);


                    }
                }
            }
            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            lstPedidos.setAdapter(listAdapter);

            lstPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position;
                    String celular = celulares[pos];
                    String url = "https://api.whatsapp.com/send?phone=55" + celular;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });

        }else {
            String nop = "NENHUM PEDIDO ACEITO!";
            lista.add(nop);
            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            lstPedidos.setAdapter(listAdapter);
        }


    }
}
