package com.example.daumhelpai;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {



    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors= null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    DataBaseHelper helper = new DataBaseHelper(this);

    public int vetor_id[],tamanhocheck,vetor_pedidos_check[];
    public ImageView imvCriar, imvAceitar, imvRecusar;
    public Bundle parametros;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        imvCriar = findViewById(R.id.imvCriar);
        imvAceitar = findViewById(R.id.imvAceitar);
        imvRecusar = findViewById(R.id.imvRecusar);
        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();

        String email = parametros.getString("email_do_usuario");
        Toast.makeText(Principal.this,"Bem VIndo",Toast.LENGTH_SHORT).show();
        listaPrincipal();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();
        String email = parametros.getString("email_do_usuario");


        listaPrincipal();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaPrincipal();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listaPrincipal();
    }

    public void listaPrincipal() {
        String email = parametros.getString("email_do_usuario");
        //fazer uma função que retorna o bitmap do banco

        int id_user = helper.buscaIdUsuario(email);
        if (id_user == 0){
            //Toast.makeText(Principal.this,"ID usuario n encontrado"+id_user,Toast.LENGTH_SHORT).show();
        }
        Cursor dadosdouser = helper.dependenciasDoUsuario(id_user); //pega id pedido aceito e id pedido recusado
        if (dadosdouser != null) {
            tamanhocheck = dadosdouser.getCount(); //tamanhocheck recebe o total de dependencias deste usuario
            vetor_pedidos_check = new int[dadosdouser.getCount()];
            int j = 0;
            while (dadosdouser.moveToNext()) {
                if (dadosdouser.getInt(0) != 0) {
                    vetor_pedidos_check[j] = dadosdouser.getInt(0);//vetor_pedidos_check recebe id aceitos
                    //Toast.makeText(Principal.this,"Pedido Aceito:"+vetor_pedidos_check[j],Toast.LENGTH_SHORT).show();
                } else {
                    vetor_pedidos_check[j] = dadosdouser.getInt(1);//vetor_pedidos_check recebe id recusado
                    //Toast.makeText(Principal.this,"Pedido Recusado:"+vetor_pedidos_check[j],Toast.LENGTH_SHORT).show();
                }
                j = j + 1;
            }
        }else{
            // não achou dependencias
            //Toast.makeText(Principal.this,"Não Encontrou Dependencias:",Toast.LENGTH_SHORT).show();
        }
        //agora temos em vetor_pedidos_check[] todos os pedidos aceitos ou recusados do usuario
        //basta verificar agora e não listar na principal


        byte[] byteArray;
        Bitmap bm;
        String strTitulo, strDescricao, strPagamento, strTipo;
        models = new ArrayList<>();
        Cursor dados = helper.listarPedidosBD(); //pega todos os pedidos do banco de dados para listar
        if (dados != null) {
            //Toast.makeText(Principal.this,"Tem pedidos criados:",Toast.LENGTH_SHORT).show();
            vetor_id = new int[dados.getCount()];  // cria um vetor com o tamanho de pedidos criados
            int i = 0;
            int j, result = 0;
            int idd=0, check=0;
            //for(i=0;i<dados.getCount();i++) {
            while (dados.moveToNext()){
                vetor_id[i] = Integer.parseInt(dados.getString(0)); //pega o id do pedido
                idd = vetor_id[i];

                for (j = 0; j < tamanhocheck; j++) {
                    check = vetor_pedidos_check[j];
                    if (idd == check) {//verifica se pedido ja foi aceito ou recusado pelo usuario
                        result = result + 1;
                    }
                }
                if (result >= 1) { //se achou dependencia, não lista
                    i = i + 1;
                    result = 0;
                }else { //lista pedido
                    strTitulo = dados.getString(1);
                    strDescricao = "Descrição: " + dados.getString(2);
                    strPagamento = dados.getString(4);
                    strTipo = dados.getString(5);
                    byteArray = dados.getBlob(3);

                    bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                    models.add(new Model(bm, strTitulo, strDescricao, strPagamento));
                    i = i + 1;
                }

            }


                adapter = new Adapter(models, this);

                viewPager = findViewById(R.id.viewPager);
                viewPager.setAdapter((PagerAdapter) adapter);
                viewPager.setPadding(130, 0, 130, 0);

                Integer[] colors_temp = {
                        getResources().getColor(R.color.color1),
                        getResources().getColor(R.color.color2),
                        getResources().getColor(R.color.color3),
                        getResources().getColor(R.color.color4),

                };

                colors = colors_temp;

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, final float positionOffset, int positionOffsetPixels) {
                        if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                            viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                        } else {
                            viewPager.setBackgroundColor(colors[colors.length - 1]);

                        }
                        final int pos = position;
                        imvAceitar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (positionOffset == 0) {
                                    String email = parametros.getString("email_do_usuario");
                                    int id_pedido = vetor_id[pos];
                                    int status;

                                    status = helper.verificaSelf(id_pedido, email);

                                    if (status == 0) {
                                        Toast.makeText(Principal.this, "Scrolled Você não pode aceitar o próprio pedido", Toast.LENGTH_SHORT).show();
                                    } else if (status == 1) {
                                        //pode proceguir
                                        int id_user = helper.buscaIdUsuario(email);
                                        if (id_user != 0) {
                                            helper.registrarPedidoAceito(id_user, id_pedido);//registra pedido na tabela
                                            atualiza();//retira pedido da principal para aquele usuario atualizando
                                            //notifica o usuario que fez o pedido

                                            //aqui que vc vai usar sua função Paulo

                                        }

                                    }
                                }
                            }
                        });
                        imvRecusar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (positionOffset == 0) {
                                    //verifica se pedido não pertence a você mesmo
                                    String email = parametros.getString("email_do_usuario");
                                    int id_pedido = vetor_id[pos];
                                    int status;

                                    status = helper.verificaSelf(id_pedido, email);

                                    if (status == 0) {
                                        Toast.makeText(Principal.this, " Scrolled Você não pode Recusar o próprio pedido", Toast.LENGTH_SHORT).show();
                                    } else if (status == 1) {
                                        //pode proceguir
                                        int id_user = helper.buscaIdUsuario(email);
                                        if (id_user != 0) {
                                            helper.registrarPedidoRecusado(id_user, id_pedido);//registra pedido na tabela
                                            atualiza();//retira pedido da principal para aquele usuario atualizando
                                            //notifica o usuario que fez o pedido

                                        }

                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onPageSelected(final int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });



        }



    }

    private void atualiza() {
        listaPrincipal();
    }

    public void criarPedido(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(Principal.this, CriarPedidos.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario",email);

        intent.putExtras(parametros);
        startActivity(intent);

    }

    public void configuracoes(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(Principal.this,Configuracoes.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario",email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    public void abrirMensagens(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(Principal.this,Mensagens.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario",email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

}
