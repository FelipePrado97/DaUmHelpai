package com.example.daumhelpai;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

    public ImageView imvCriar, imvAceitar, imvRecusar;
    public Bundle parametros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        imvCriar = findViewById(R.id.imvCriar);
        imvAceitar = findViewById(R.id.imvAceitar);
        imvRecusar = findViewById(R.id.imvRecusar);

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();



        models = new ArrayList<>();
        models.add(new Model(R.drawable.brochure,"Brochure","brdasdasjdals"));
        models.add(new Model(R.drawable.sticker,"Sticker","asdasdasd"));
        models.add(new Model(R.drawable.poster,"Poster","ewrwerwerw"));
        models.add(new Model(R.drawable.namecard,"Namecard","qewqewqwe"));

        adapter = new Adapter(models,this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter((PagerAdapter) adapter);
        viewPager.setPadding(130,0,130,0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),

        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position<(adapter.getCount()-1) && position < (colors.length - 1)){
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                }else{
                    viewPager.setBackgroundColor(colors[colors.length -1 ]);

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



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
